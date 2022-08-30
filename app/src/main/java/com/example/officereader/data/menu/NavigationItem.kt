package com.example.officereader.data.menu

import com.example.officereader.R


sealed class NavigationItem(var route: String, var icon: Int, var title: String,val idx: Int){
    object Home: NavigationItem("home", R.drawable.baseline_home_24,"HOME",0)
    object Recent: NavigationItem("recent", R.drawable.baseline_history_24,"RECENT",1)
    object Favorite: NavigationItem("favorite", R.drawable.baseline_favorite_24,"FAVORITE",2)
    object Setting: NavigationItem("setting", R.drawable.baseline_settings_24,"SETTING",3)
}