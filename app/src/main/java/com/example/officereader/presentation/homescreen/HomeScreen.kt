package com.example.officereader.presentation.homescreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DrawerValue
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberDrawerState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.officereader.*
import com.example.officereader.data.menu.NavigationItem
import com.example.officereader.presentation.navigationscreen.BottomNavigationBar
import com.example.officereader.presentation.navigationscreen.Tabs
import com.example.officereader.presentation.navigationscreen.TabsContent
import com.example.officereader.presentation.navigationscreen.TopBar
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState

@Composable
fun HomeScreen() {
    val navController = rememberNavController()
    val direction = remember { mutableStateOf(-1) }
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val viewModel: HomeViewModel = viewModel()
    Scaffold(
        topBar = { TopBar(scope,scaffoldState) },
        bottomBar = { BottomNavigationBar(viewModel, navController) },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues)
            ) {
                NavigationApp(navController = navController, viewModel)
            }
        },
        backgroundColor = colorResource(
            id = R.color.purple_500
        )
    )
}

@Composable
fun NavigationApp(navController: NavHostController, viewModel: HomeViewModel) {
    NavHost(navController = navController, startDestination = NavigationItem.Home.route) {
        composable(NavigationItem.Home.route) {
            NavHomeScreen(viewModel)
        }
        composable(NavigationItem.Recent.route) {
            DocxScreen()
        }
        composable(NavigationItem.Favorite.route) {
            PPTScreen()
        }
        composable(NavigationItem.Setting.route) {
            SettingScreen()
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun NavHomeScreen(viewModel: HomeViewModel) {
    val tabs = viewModel.state.value.menuTabBar
    val pagerState = rememberPagerState()
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Tabs(tabs = tabs, pagerState = pagerState)
        TabsContent(tabs = tabs, pagerState = pagerState)
    }
}