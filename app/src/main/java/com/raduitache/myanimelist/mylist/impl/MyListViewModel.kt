package com.raduitache.myanimelist.mylist.impl

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyListViewModel @Inject constructor(): ViewModel() {
    val isSignedIn: Boolean = false
}