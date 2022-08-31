package com.example.officereader.presentation.navigationscreen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.officereader.R
import com.example.officereader.data.menu.NavigationItem
import com.example.officereader.data.menu.TabItem
import com.example.officereader.presentation.homescreen.HomeViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.pagerTabIndicatorOffset
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TopBar(scope: CoroutineScope, scaffoldState: ScaffoldState, state: ModalBottomSheetState) {
    TopAppBar(
        title = { Text(text = stringResource(R.string.app_name), fontSize = 18.sp) },
        navigationIcon = {
            IconButton(onClick = {
                Log.i("Draw CLick", "xxx")
                scope.launch {
                    scaffoldState.drawerState.open()
//                    state.show()
                }
            }) {
                Icon(Icons.Filled.Menu, "")
            }
        },
        backgroundColor = colorResource(
            id = R.color.purple_200
        ), contentColor = Color.White
    )
}

@Composable
fun BottomNavigationBar(viewModel: HomeViewModel, navController: NavController) {
    val items = viewModel.state.value.menuBottomBar.values.toList()
    BottomNavigation(
        backgroundColor = colorResource(id = R.color.purple_200),
        contentColor = Color.White
    ) {
        items.forEach { item ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.title
                    )
                },
//                label = { Text(text = item.title) },
                selectedContentColor = Color.White,
                unselectedContentColor = Color.White.copy(0.4f),
                alwaysShowLabel = true,
                selected = item.idx == viewModel.state.value.numberActive,
                onClick = {
                    FwScreen(viewModel, navController, item)
                })
        }
    }
}

fun FwScreen(viewModel: HomeViewModel, navController: NavController, item: NavigationItem) {
    navController.navigate(item.route) {
        // Pop up to the start destination of the graph to
        // avoid building up a large stack of destinations
        // on the back stack as users select items
        navController.graph.startDestinationRoute?.let { route ->
            popUpTo(route) {
                saveState = true
            }
        }
        // Avoid multiple copies of the same destination when
        // reselecting the same item
        launchSingleTop = true
        // Restore state when reselecting a previously selected item
        restoreState = true
        viewModel.updateActiveScreen(item.idx)
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Tabs(tabs: List<TabItem>, pagerState: PagerState) {
    val scope = rememberCoroutineScope()
    TabRow(
        selectedTabIndex = pagerState.currentPage,
        backgroundColor = colorResource(id = R.color.colorPrimaryDark),
        contentColor = Color.White,
        indicator = { tabPosition ->
            TabRowDefaults.Indicator(Modifier.pagerTabIndicatorOffset(pagerState, tabPosition))
        }) {
        tabs.forEachIndexed { index, tabItem ->
            LeadingIconTab(
                selected = pagerState.currentPage == index,
                onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
                text = {},
                icon = {
                    Icon(
                        painter = painterResource(id = tabItem.icon),
                        contentDescription = ""
                    )
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@ExperimentalPagerApi
@Composable
fun TabsContent(tabs: List<TabItem>, pagerState: PagerState) {
    HorizontalPager(count = tabs.size, state = pagerState) { page ->
        tabs[page].screen()
    }
}

@Composable
fun Drawer(scope: CoroutineScope, scaffoldState: ScaffoldState, navController: NavController) {
    val items = listOf(
        NavigationItem.Home,
        NavigationItem.Recent,
        NavigationItem.Favorite,
        NavigationItem.Setting
    )
    Column(modifier = Modifier.background(colorResource(id = R.color.purple_500))) {
        // Header
        Image(
            painter = painterResource(id = R.drawable.baseline_home_24),
            contentDescription = "Vangck",
            modifier = Modifier
                .height(100.dp)
                .fillMaxWidth()
                .padding(10.dp),
            colorFilter = ColorFilter.tint(Color.White)
        )
        // Space between
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(5.dp)
        )

        //List of navigation items
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            DrawerItem(item = item, selected = currentRoute == item.route, onItemClick = {
                navController.navigate(item.route) {
                    // Pop up to the start destination of the graph to
                    // avoid building up a large stack of destinations
                    // on the back stack as users select items
                    navController.graph.startDestinationRoute?.let { route ->
                        popUpTo(route) {
                            saveState = true
                        }
                    }
                    // Avoid multiple copies of the same destination when
                    // reselecting the same item
                    launchSingleTop = true
                    // Restore state when reselecting a previously selected item
                    restoreState = true
                }
                // Close drawer
                scope.launch {
                    scaffoldState.drawerState.close()
                }
            })
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "Vangck",
            color = Color.White,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(12.dp)
                .align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun DrawerItem(item: NavigationItem, selected: Boolean, onItemClick: (NavigationItem) -> Unit) {
    val background = if (selected) R.color.colorPrimary else android.R.color.transparent
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { onItemClick(item) })
            .height(45.dp)
            .background(colorResource(id = background))
            .padding(start = 10.dp)
    ) {
        Image(
            painter = painterResource(id = item.icon),
            contentDescription = item.title,
            colorFilter = ColorFilter.tint(Color.White),
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .height(35.dp)
                .width(35.dp)
        )
        Spacer(modifier = Modifier.width(7.dp))
        Text(
            text = item.title,
            fontSize = 18.sp,
            color = Color.White
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Preview(showBackground = true)
@Composable
fun TabsPreview() {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val navController = rememberNavController()
    Drawer(scope = scope, scaffoldState = scaffoldState, navController = navController)
}
