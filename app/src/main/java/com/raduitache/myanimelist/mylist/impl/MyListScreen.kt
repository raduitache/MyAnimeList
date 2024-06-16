package com.raduitache.myanimelist.mylist.impl

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.raduitache.myanimelist.responses.Anime
import com.raduitache.myanimelist.responses.Response

@Composable
fun MyListScreen(onAnimeClick: (String) -> Unit, authScreen: @Composable () -> Unit, viewModel: MyListViewModel = hiltViewModel()) {
    val uiData by viewModel.uiData.collectAsState()

    if (uiData.isSignedIn) {
        ListScreen(uiData, onAnimeClick, viewModel::updateAnimeProgress)
    } else {
        authScreen()
    }
}

@Composable
private fun MyListItem(data: Response.DataItem<Anime>, onAnimeClick: (String) -> Unit, updateAnimeProgress: (Int, Int) -> Unit) {
    ListItem(
        headlineContent = {
            Column {
                Text(
                    text = data.node.title,
                    style = MaterialTheme.typography.titleLarge,
                )

                Text(
                    text = data.node.startSeason?.let { "${data.node.mediaType}, ${it.season} ${it.year}" } ?: data.node.mediaType,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.secondary,
                )
            }
        },
        modifier = Modifier.clickable { onAnimeClick(data.node.id.toString()) },
        supportingContent = {
            Column(horizontalAlignment = Alignment.End) {
                LinearProgressIndicator(
                    progress = {
                        if (data.node.numEpisodes == 0 || data.listStatus == null) {
                            if (data.listStatus?.status == "watching") 0.5f else 0f
                        } else {
                            data.listStatus.numEpisodesWatched.toFloat() / data.node.numEpisodes
                        }
                    },
                    modifier = Modifier
                        .height(16.dp)
                        .fillMaxWidth(),
                )

                Text(
                    text = "${data.listStatus?.numEpisodesWatched ?: 0} / ${ if (data.node.numEpisodes > 0) data.node.numEpisodes else "??" } ep",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.secondary,
                )
            }
        },
        leadingContent = {
            AsyncImage(
                model = data.node.mainPicture?.medium, contentDescription = null
            )
        },
        trailingContent = {
            IconButton(onClick = { updateAnimeProgress(data.node.id, (data.listStatus?.numEpisodesWatched ?: 0) + 1) }) {
                Icon(
                    painter = rememberVectorPainter(image = Icons.Outlined.Add),
                    contentDescription = "Increase number of watched anime",
                )
            }
        },
    )
}

@Composable
private fun ListScreen(uiData: MyListUiData, onAnimeClick: (String) -> Unit, updateAnimeProgress: (Int, Int) -> Unit) {
    val pagingData = uiData.pagingDataFlow.collectAsLazyPagingItems()

    Scaffold { innerPadding ->
        LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = innerPadding) {
            if (pagingData.loadState.refresh == LoadState.Loading) {
                item {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.CenterHorizontally)
                    )
                }
            }

            items(pagingData.itemCount) { index ->
                pagingData[index]?.let {
                    MyListItem(data = it,  onAnimeClick = onAnimeClick, updateAnimeProgress = updateAnimeProgress)
                }
            }

            if (pagingData.loadState.append == LoadState.Loading) {
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