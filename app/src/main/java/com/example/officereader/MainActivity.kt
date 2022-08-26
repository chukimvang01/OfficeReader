package com.example.officereader

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.officereader.ui.theme.OfficeReaderTheme
import java.lang.Math.abs

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OfficeReaderTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    HomeScreen()
                }
            }
        }
    }
}

@Composable
fun NavigationApp(navController: NavHostController) {
    NavHost(navController = navController, startDestination = NavigationItem.Pdf.route) {
        composable(NavigationItem.Pdf.route) {
            PdfScreen()
        }
        composable(NavigationItem.Docx.route) {
            DocxScreen()
        }
        composable(NavigationItem.Ppt.route) {
            PPTScreen()
        }
        composable(NavigationItem.Txt.route) {
            TxtScreen()
        }
        composable(NavigationItem.Excel.route) {
            ExcelScreen()
        }
    }
}

@Composable
fun TopBar() {
    TopAppBar(
        title = { Text(text = stringResource(R.string.app_name), fontSize = 18.sp) },
        backgroundColor = colorResource(
            id = R.color.purple_200
        ), contentColor = Color.White
    )
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        NavigationItem.Pdf,
        NavigationItem.Docx,
        NavigationItem.Txt,
        NavigationItem.Ppt,
        NavigationItem.Excel
    )
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
                label = { Text(text = item.title) },
                selectedContentColor = Color.White,
                unselectedContentColor = Color.White.copy(0.4f),
                alwaysShowLabel = true,
                selected = false,
                onClick = {
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
                })
        }
    }
}

@Composable
fun HomeScreen() {
    val navController = rememberNavController()
    var direction = remember { mutableStateOf(-1) }
    print("start homescreen")
    Scaffold(
        topBar = { TopBar() },
        bottomBar = { BottomNavigationBar(navController) },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDrag = { change, dragAmount ->
                                change.consume()

                                val (x, y) = dragAmount
                                if (abs(x) > abs(y)) {
                                    when {
                                        x > 0 -> {
                                            //right
                                            direction.value = 0
                                        }
                                        x < 0 -> {
                                            // left
                                            direction.value = 1
                                        }
                                    }
                                } else {
                                    when {
                                        y > 0 -> {
                                            // down
                                            direction.value = 2
                                        }
                                        y < 0 -> {
                                            // up
                                            direction.value = 3
                                        }
                                    }
                                }

                            },
                            onDragEnd = {
                                when (direction.value){
                                    0 -> {
                                        //right swipe code here
                                        Log.i("Swipe: ","swipe right")
                                        }
                                        1 -> {
                                            // left swipe code here
                                            Log.i("Swipe: ","swipe left")
                                        }
                                        2 -> {
                                            // down swipe code here
                                            Log.i("Swipe: ","swipe down")
                                        }
                                        3 -> {
                                            // up swipe code here
                                            Log.i("Swipe: ","swipe up")
                                        }
                                    }
                            })
                    }) {
                NavigationApp(navController = navController)
            }
        },
        backgroundColor = colorResource(
            id = R.color.purple_500
        )
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    OfficeReaderTheme {
        HomeScreen()
    }
}

@Composable
fun PdfScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.colorPrimaryDark))
            .wrapContentSize(Alignment.Center)
    ) {
        Text(
            text = "PDF View",
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center,
            fontSize = 25.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PDFScreenPreview() {
    PdfScreen()
}

@Composable
fun DocxScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.colorPrimaryDark))
            .wrapContentSize(Alignment.Center)
    ) {
        Text(
            text = "Docx View",
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center,
            fontSize = 25.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DocxScreenPreview() {
    DocxScreen()
}

@Composable
fun PPTScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.colorPrimaryDark))
            .wrapContentSize(Alignment.Center)
    ) {
        Text(
            text = "PPT View",
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center,
            fontSize = 25.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PptScreenPreview() {
    PPTScreen()
}


@Composable
fun TxtScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.colorPrimaryDark))
            .wrapContentSize(Alignment.Center)
    ) {
        Text(
            text = "TXT View",
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center,
            fontSize = 25.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BooksScreenPreview() {
    TxtScreen()
}

@Composable
fun ExcelScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.colorPrimaryDark))
            .wrapContentSize(Alignment.Center)
    ) {
        Text(
            text = "Excel View",
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center,
            fontSize = 25.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ExcelScreen()
}