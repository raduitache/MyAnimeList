package com.raduitache.myanimelist.forum.impl

import android.app.Activity
import android.net.Uri
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import com.raduitache.myanimelist.common.CodeChallenge
import com.raduitache.myanimelist.forum.impl.paging.ForumPagingSource
import com.raduitache.myanimelist.forum.models.Categories
import com.raduitache.myanimelist.forum.models.Data
import com.raduitache.myanimelist.services.ServicesConstants
import com.raduitache.myanimelist.services.forum.ForumService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.awaitResponse
import javax.inject.Inject

@Composable
fun ForumScreen(
    forumScreenViewModel: ForumScreenViewModel = hiltViewModel()
) {
    var isExpanded by rememberSaveable {
        mutableStateOf<String?>(null)
    }
    val lazyPagingItems =
        forumScreenViewModel.pager.collectAsState().value?.flow?.collectAsLazyPagingItems()

    if (lazyPagingItems != null) {
        BackHandler {
            forumScreenViewModel.clearData()
        }
    }
    if (lazyPagingItems?.loadState?.refresh == LoadState.Loading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (lazyPagingItems == null) {
            item {
                if (forumScreenViewModel.isLoading.collectAsState().value) {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else {
                    for (item in forumScreenViewModel.topics.collectAsState().value) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                println(item.toString())
                                isExpanded = item.title
                            }) {
                            Text(text = item.title ?: "No", modifier = Modifier.padding(8.dp))
                            if (isExpanded == item.title) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    modifier = Modifier.horizontalScroll(rememberScrollState())
                                ) {
                                    for (board in item.boards) {
                                        AssistChip(onClick = {
                                            forumScreenViewModel.getData(board.id!!)
                                        }, label = { Text(text = board.title ?: "") })
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (lazyPagingItems != null) {
            items(
                count = lazyPagingItems.itemCount,
                key = lazyPagingItems.itemKey { it.id ?: 0 }) { index ->
                val item = lazyPagingItems[index]
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val activity = LocalContext.current as Activity
                    Card(
                        modifier = Modifier.clickable {
                            val customTabsIntent = CustomTabsIntent.Builder().build()

                            val codeChallenge = CodeChallenge()
                            Log.d("SignInViewModel", "initial challenge: $codeChallenge")
                            customTabsIntent.launchUrl(
                                activity,
                                Uri.parse(
                                    "https://myanimelist.net/forum/?topicid=${item?.id}"
                                ),
                            )
                            println()
                        }
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = item?.title ?: "")
                            Text(text = item?.createdBy?.name ?: "", modifier = Modifier.weight(1f))
                        }
                    }
                }
            }

            if (lazyPagingItems.loadState.append == LoadState.Loading) {
                item {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.CenterHorizontally)
                    )
                }
            }

        }

    }
}

@HiltViewModel
class ForumScreenViewModel @Inject constructor(
    private val forumServiceFlow: Flow<@JvmSuppressWildcards ForumService>,
) : ViewModel() {
    val isLoading = MutableStateFlow(false);
    val topics = MutableStateFlow<List<Categories>>(emptyList())
    val pager = MutableStateFlow<Pager<Int, Data>?>(null)

    init {
        viewModelScope.launch {
            isLoading.update {
                true
            }
            try {
                val service = forumServiceFlow.firstOrNull() ?: return@launch
                val response = service.getBoards().awaitResponse();
                val body = response.body() ?: return@launch
                topics.update {
                    body.categories
                }
            } catch (e: Exception) {
                println(e.stackTraceToString())
            }
            isLoading.update {
                false
            }
        }
    }

    fun getData(boardId: Int) {
        viewModelScope.launch {
            try {
                val service = forumServiceFlow.firstOrNull() ?: return@launch
                pager.update {
                    Pager(
                        config = PagingConfig(pageSize = ServicesConstants.REQUEST_ITEM_COUNT),
                        initialKey = 0,
                    ) {
                        ForumPagingSource(
                            forumService = service,
                            boardId = boardId,
                        )
                    }
                }
            } catch (e: Exception) {
                println(e.stackTraceToString())
            }
        }
    }

    fun clearData() {
        pager.update {
            null
        }
    }

}