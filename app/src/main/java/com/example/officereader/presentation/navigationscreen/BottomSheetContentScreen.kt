package com.example.officereader.presentation.navigationscreen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.officereader.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun BottomSheetListItem(icon: Int, title: String, onItemClick: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { onItemClick(title) })
            .height(55.dp)
            .background(
                color = colorResource(
                    id = R.color.colorPrimary
                )
            )
            .padding(start = 15.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(painter = painterResource(id = icon), contentDescription = "Share", tint = Color.White)
        Spacer(modifier = Modifier.width(20.dp))
        Text(text = title, color = Color.White)
    }
}

@Composable
fun BottomSheetContent(){
    Column {
        BottomSheetListItem(icon = R.drawable.baseline_slideshow_24, title = "Share", onItemClick = { Log.i("SHEET BOTTOM","Share Clicked!")})
        BottomSheetListItem(icon = R.drawable.baseline_history_24, title = "Get link", onItemClick = { Log.i("SHEET BOTTOM","Get link!")})
        BottomSheetListItem(icon = R.drawable.baseline_assignment_24, title = "Edit name", onItemClick = { Log.i("SHEET BOTTOM","Edit name!")})
        BottomSheetListItem(icon = R.drawable.baseline_settings_24, title = "Delete collection", onItemClick = { Log.i("SHEET BOTTOM","Delete collection!")})
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ModalBottomSheetLayoutScreen() {
    val navController = rememberNavController()
    val modalBottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    val stateScaff = rememberScaffoldState()
    ModalBottomSheetLayout(
        sheetContent = {
            BottomSheetContent()
        },
        sheetState = modalBottomSheetState,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetBackgroundColor = colorResource(id = R.color.colorPrimary),
        // scrimColor = Color.Red,  // Color for the fade background when you open/close the drawer
    ) {
        Scaffold(
            scaffoldState = stateScaff,
            topBar = { TopBar(scope,stateScaff,modalBottomSheetState) },
            drawerBackgroundColor = colorResource(id = R.color.colorPrimary),
            drawerContent = {
                Drawer(scope = scope, scaffoldState = stateScaff, navController = navController)
            },
            backgroundColor = colorResource(id = R.color.colorPrimaryDark)
        ) { padding ->  // We need to pass scaffold's inner padding to content. That's why we use Box.
            Box(modifier = Modifier.padding(padding)) {
                ModalBottomSheetMainScreen(scope = scope, state = modalBottomSheetState)
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ModalBottomSheetMainScreen(scope: CoroutineScope, state: ModalBottomSheetState) {
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            colors = ButtonDefaults.buttonColors(
                backgroundColor = colorResource(id = R.color.colorPrimary),
                contentColor = Color.White
            ),
            onClick = {
                scope.launch {
                    state.show()
                }
            }) {
            Text(text = "Open Modal Bottom Sheet Layout")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomSheetListPreview(){
    BottomSheetContent()
}