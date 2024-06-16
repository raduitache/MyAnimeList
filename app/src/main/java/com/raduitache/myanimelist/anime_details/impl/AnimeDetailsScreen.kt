package com.raduitache.myanimelist.anime_details.impl

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimeDetailsScreen(onBack: () -> Unit, animeDetailsViewModel: AnimeDetailsViewModel = hiltViewModel()) {
    val screenState = animeDetailsViewModel.animeDetailsScreenState.collectAsState().value
    if (screenState.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }
    val selectedAnime = screenState.selectedAnime ?: return
    var selectedImage by remember {
        mutableStateOf<String?>(null)
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopAppBar(title = {}, navigationIcon = {
                BackHandler {
                    onBack()
                }
                IconButton(onClick = { onBack() }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                }
            })
            AsyncImage(model = selectedAnime.mainPicture.large ?: "", contentDescription = null)
            Text(text = selectedAnime.title + if (!selectedAnime.mean.isNullOrBlank()) " ${selectedAnime.mean} ⭐" else "", textAlign = TextAlign.Center)
            selectedAnime.myList?.let {
                Text(text = it.status)
            }
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.Start
            ) {
                val n = 250
                val biggerThan = (selectedAnime.synopsis?.length ?: 0) > n
                var trimSynopsis by remember {
                    mutableStateOf(biggerThan)
                }
                val clickableModifier = if (biggerThan) {
                    Modifier.clickable {
                        trimSynopsis = !trimSynopsis
                    }
                } else {
                    Modifier
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Synopsis", modifier = Modifier.padding(start = 8.dp), style = MaterialTheme.typography.headlineMedium)
                    if (biggerThan) {
                        IconButton(onClick = { trimSynopsis = !trimSynopsis }) {
                            Icon(imageVector = if (trimSynopsis) Icons.Default.ArrowDropDown else Icons.Default.KeyboardArrowUp, contentDescription = null )
                        }
                    }

                }
                val s = selectedAnime.synopsis ?: ""
                Text(text = if (trimSynopsis) s.substring(0, s.length.coerceAtMost(n)) + "...\n Press for more..." else s, clickableModifier.padding(4.dp))
            }
            selectedAnime.pictures.let { list ->
                LazyRow(
                    modifier = Modifier.padding(start = 4.dp)
                ) {
                    items(list.filter { it != selectedAnime.mainPicture }) {
                        AsyncImage(model = it.medium, contentDescription = null, modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .clickable { selectedImage = it.medium })
                    }
                }
            }
            Text(text = "Aired from ${selectedAnime.startDate ?: "TBA"} to ${selectedAnime.endDate ?: "Not finished yet"}")
            Text(text = "Mean = ${selectedAnime.mean}")
            Text(text = "Popularity = ${selectedAnime.popularity}")
            Text(text = "Others")
            Text(text = "watching = ${selectedAnime.statistics.from.watching}")
            Text(text = "plan to watch = ${selectedAnime.statistics.from.plan_to_watch}")
            Text(text = "completed = ${selectedAnime.statistics.from.completed}")
            Text(text = "dropped = ${selectedAnime.statistics.from.dropped}")
            Text(text = "on_hold = ${selectedAnime.statistics.from.on_hold}")
        }
        selectedImage?.let {
            Box(modifier = Modifier
                .fillMaxSize()
                .clickable(interactionSource = remember {
                    MutableInteractionSource()
                }, indication = null, onClick = {
                    selectedImage = null
                })
                .background(
                    Color.Black.copy(alpha = 0.8f)
                )) {
                AsyncImage(model = it, contentDescription = null, modifier = Modifier.fillMaxSize())
            }

        }

    }
    
}