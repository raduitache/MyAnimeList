package com.raduitache.myanimelist.anime_details

import com.raduitache.myanimelist.main.NavGraph

abstract class AnimeNavGraph(graphRoute: AnimeGraphRoute, startDestination: AnimeNavRoute): NavGraph(graphRoute, startDestination)