package com.example.officereader.presentation.homescreen

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import com.example.officereader.data.menu.NavigationItem
import com.example.officereader.data.menu.TabItem

data class HomeScreenState(val menuBottomBar: Map<Int, NavigationItem>, val menuTabBar: List<TabItem>, val numberActive: Int, val error: String? = null)
