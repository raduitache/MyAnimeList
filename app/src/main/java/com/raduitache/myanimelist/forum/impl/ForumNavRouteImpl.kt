package com.raduitache.myanimelist.forum.impl

import androidx.compose.runtime.Composable
import com.raduitache.myanimelist.forum.ForumNavRoute
import javax.inject.Inject

class ForumNavRouteImpl @Inject constructor(): ForumNavRoute("Forum/Forum", emptyList()){
    @Composable
    override fun Content() {
        ForumScreen()
    }
}