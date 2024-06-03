package com.raduitache.myanimelist.seasonal.impl

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import com.raduitache.myanimelist.seasonal.Season

@Composable
fun SeasonalScreen(goNext: (String) -> Unit, viewModel: SeasonalViewModel = hiltViewModel()) {
    val lazyPagingItems = viewModel.pager.collectAsLazyPagingItems()
    val screenState = viewModel.screenState.collectAsState().value
    var isExpanded by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Box(
                        modifier = Modifier.clickable {
                            isExpanded = isExpanded.not()
                        },
                        contentAlignment = Alignment.Center
                    ) {
                        SeasonText(
                            season = screenState.selectedSeason,
                            year = screenState.selectedYear,
                            replaceWithEmojis = true
                        )
                        DropdownMenu(
                            expanded = isExpanded,
                            onDismissRequest = { isExpanded = false }) {
                            for (i in viewModel.generateLastSeasons()) {
                                SeasonText(
                                    season = i.first,
                                    year = i.second,
                                    modifier = Modifier.clickable {
                                        isExpanded = false
                                        viewModel.changeSeasonTo(i.first, i.second)
                                    }
                                )
                            }
                        }
                    }
                    Text(text = screenState.sorting.toString(), modifier = Modifier.clickable {
                        viewModel.changeSorting()
                    }, style = MaterialTheme.typography.labelMedium)
                }

            }
            items(
                count = lazyPagingItems.itemCount,
                key = lazyPagingItems.itemKey { it.id }) { index ->
                val item = lazyPagingItems[index]
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            goNext(item?.id.toString())
                        }
                        .padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    AsyncImage(
                        model = item?.mainPicture?.medium ?: "",
                        contentDescription = null,
                    )
                    Text(item?.title ?: "", textAlign = TextAlign.Center)
                    Text(item?.mean?.toString() ?: "")
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

        if (lazyPagingItems.loadState.refresh == LoadState.Loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }


    }

}

@Composable
private fun SeasonText(season: Season, year: Int, replaceWithEmojis: Boolean = false, modifier: Modifier = Modifier) {
    Text(
        text = "${
            if (replaceWithEmojis) 
                when (season) {
                    Season.WINTER -> "❄️"
                    Season.SPRING -> "🌱"
                    Season.SUMMER -> "🏖️"
                    Season.FALL -> "🍁"
                }
                else 
            season.toString().lowercase()
                .replaceFirstChar { it.uppercaseChar() }
        } ${if(replaceWithEmojis) "" else "-"} $year",
        modifier = modifier
    )
}
