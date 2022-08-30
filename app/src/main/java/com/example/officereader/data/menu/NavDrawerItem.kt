package com.example.officereader.data.menu

import com.example.officereader.R

sealed class NavDrawerItem(var route: String, var icon: Int, var title: String){
    object Home: NavDrawerItem("home", R.drawable.baseline_home_24,"Home")
    object Recent: NavDrawerItem("recent", R.drawable.baseline_history_24,"Recent")
    object Favorite: NavDrawerItem("favorite", R.drawable.baseline_favorite_24,"Favorite")
    object Setting: NavDrawerItem("setting", R.drawable.baseline_settings_24,"Setting")
}
