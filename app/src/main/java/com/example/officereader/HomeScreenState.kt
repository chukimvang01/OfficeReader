package com.example.officereader

data class HomeScreenState(val menuBottomBar: Map<Int,NavigationItem>, val numberActive: Int, val error: String? = null)
