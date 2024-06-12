package com.raduitache.myanimelist.mylist.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.google.firebase.auth.FirebaseUser
import com.raduitache.myanimelist.mylist.impl.paging.MyListPagingSource
import com.raduitache.myanimelist.services.ServicesConstants
import com.raduitache.myanimelist.services.mylist.MyListService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import retrofit2.await
import javax.inject.Inject

@HiltViewModel
class MyListViewModel @Inject constructor(
    currentUserFlow: Flow<@JvmSuppressWildcards FirebaseUser?>,
    private val myListServiceFlow: Flow<@JvmSuppressWildcards MyListService>,
) : ViewModel() {
    private val forceRefresh: MutableSharedFlow<Unit> = MutableSharedFlow<Unit>(replay = 1).apply {
        tryEmit(Unit)
    }
    @OptIn(ExperimentalCoroutinesApi::class)
    val uiData: StateFlow<MyListUiData> =
        forceRefresh.flatMapLatest {
            combine(currentUserFlow, myListServiceFlow) { user, service ->
                if (user == null) return@combine MyListUiData(isSignedIn = false)

                MyListUiData(
                    isSignedIn = true,
                    pagingDataFlow = Pager(
                        config = PagingConfig(pageSize = ServicesConstants.REQUEST_ITEM_COUNT),
                        initialKey = 0,
                    ) { MyListPagingSource(service = service) }.flow,
                )
            }
        }
            .stateIn(viewModelScope, SharingStarted.Lazily, MyListUiData(isSignedIn = false))

    fun updateAnimeProgress(animeId: Int, watchedEpisodes: Int) {
        viewModelScope.launch {
            myListServiceFlow.first().updateAnimeProgress(animeId, watchedEpisodes).await()

            forceRefresh.emit(Unit)
        }
    }
}