package com.raduitache.myanimelist.anime_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raduitache.myanimelist.responses.Anime
import com.raduitache.myanimelist.services.anime.AnimeService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.await
import javax.inject.Inject

@HiltViewModel
class AnimeDetailsViewModel @Inject constructor(
    private val animeServiceFlow: Flow<@JvmSuppressWildcards AnimeService>
) : ViewModel() {
    private val _animeDetailsScreenState = MutableStateFlow(AnimeDetailsScreenState())
    val animeDetailsScreenState = _animeDetailsScreenState.asStateFlow()

    fun selectAnime(id: String) {
        viewModelScope.launch {
            _animeDetailsScreenState.update {
                it.copy(isLoading = true)
            }
            try {
                val service = animeServiceFlow.firstOrNull() ?: return@launch
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
    }

}

data class AnimeDetailsScreenState (
    val isLoading: Boolean = false,
    val selectedAnime: Anime? = null
)