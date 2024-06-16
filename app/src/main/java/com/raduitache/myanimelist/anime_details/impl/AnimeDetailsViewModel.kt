package com.raduitache.myanimelist.anime_details.impl

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.raduitache.myanimelist.responses.Anime
import com.raduitache.myanimelist.seasonal.WatchingState
import com.raduitache.myanimelist.services.anime.AnimeService
import com.raduitache.myanimelist.services.mylist.MyListService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.await
import javax.inject.Inject

@HiltViewModel
class AnimeDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val animeServiceFlow: Flow<@JvmSuppressWildcards AnimeService>,
    private val currentUserFlow: Flow<@JvmSuppressWildcards FirebaseUser?>,
    private val myListServiceFlow: Flow<@JvmSuppressWildcards MyListService>,
) : ViewModel() {
    private val id: String = checkNotNull(savedStateHandle["animeId"]) {
        "Invalid animeId parameter provided"
    }
    private val _animeDetailsScreenState = MutableStateFlow(AnimeDetailsScreenState())
    val animeDetailsScreenState = _animeDetailsScreenState.asStateFlow()

    val canUpdate = currentUserFlow.map { it != null }

    init {
        viewModelScope.launch {
            update()
        }
    }

    private suspend fun update() {
        _animeDetailsScreenState.update {
            it.copy(isLoading = true)
        }
        try {
            val service = animeServiceFlow.firstOrNull() ?: return
            val animeDetails = service.getSeasonalAnime(id, fields= "id,title,main_picture,alternative_titles,start_date,end_date,synopsis,mean,rank,popularity,num_list_users,num_scoring_users,nsfw,created_at,updated_at,media_type,status,genres,my_list_status,num_episodes,start_season,broadcast,source,average_episode_duration,rating,pictures,background,related_anime,related_manga,recommendations,studios,statistics").await()
            _animeDetailsScreenState.update {
                it.copy(isLoading = false, selectedAnime = animeDetails)
            }
        } catch (e: Exception) {
            _animeDetailsScreenState.update {
                it.copy(isLoading = false)
            }
        }
    }

    fun setWatching(watchingState: WatchingState) {
        viewModelScope.launch {
            val service = myListServiceFlow.firstOrNull() ?: return@launch
            service.updateAnimeStatus(id.toInt(), watchingState.name.lowercase()).await()
            update()
        }
    }

}

data class AnimeDetailsScreenState (
    val isLoading: Boolean = false,
    val selectedAnime: Anime? = null
)