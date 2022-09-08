package com.example.officereader.presentation.navigationscreen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.officereader.R
import com.example.officereader.presentation.homescreen.HomeViewModel
import com.example.officereader.ui.theme.Purple200
import com.example.officereader.ui.theme.Purple500
import com.example.officereader.ui.theme.Purple700
import java.util.*

@Composable
fun SearchView(state: MutableState<TextFieldValue>) {
    TextField(
        value = state.value,
        onValueChange = { value -> state.value = value },
        modifier = Modifier.fillMaxWidth(),
        textStyle = TextStyle(color = Color.White, fontSize = 18.sp),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "",
                modifier = Modifier
                    .padding(15.dp)
                    .size(24.dp)
            )
        },
        trailingIcon = {
            if (state.value != TextFieldValue("")) {
                IconButton(onClick = { state.value = TextFieldValue("") }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "",
                        modifier = Modifier
                            .padding(15.dp)
                            .size(24.dp)
                    )
                }
            }
        },
        singleLine = true,
        shape = RectangleShape,
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.White,
            cursorColor = Color.White,
            leadingIconColor = Color.White,
            trailingIconColor = Color.White,
            backgroundColor = colorResource(id = R.color.colorPrimaryDark),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        )
    )
}

@Composable
fun CountryListItem(countryText: String, onItemClick: (String) -> Unit) {
    Row(
        modifier = Modifier
            .clickable(onClick = { onItemClick(countryText) })
            .background(color = Purple700)
            .height(57.dp)
            .fillMaxWidth()
            .padding(paddingValues = PaddingValues(8.dp, 16.dp))
    ) {
        Text(text = countryText, fontSize = 18.sp, color = Color.White)
    }
}

@Composable
fun CountryList(navController: NavController, state: MutableState<TextFieldValue>) {
    val viewModel: HomeViewModel = viewModel()
    val countries = viewModel.getListOfCountries()
    val stateGrid = rememberLazyGridState()
    var filterCountries: ArrayList<String>
    LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 150.dp),
        state = stateGrid,
        contentPadding = PaddingValues(10.dp),
        modifier = Modifier.fillMaxWidth()) {
        val searchText = state.value.text
        filterCountries = if (searchText.isEmpty()) {
            countries
        } else {
            val result = ArrayList<String>()
            for (country in countries) {
                if (country.lowercase(Locale.getDefault())
                        .contains(searchText.lowercase(Locale.getDefault()))
                ){
                    result.add(country)
                }
            }
            result
        }
        items(filterCountries){ filterCountry ->
            CountryListItem(countryText = filterCountry, onItemClick = { selectedCountnry ->
                Log.i("Searched value",selectedCountnry)
            })
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchViewPreview() {
    val textState = remember { mutableStateOf(TextFieldValue("")) }
    SearchView(state = textState)
}

@Preview(showBackground = true)
@Composable
fun CountryListItemPreview() {
    val navController = rememberNavController()
    val textState = remember { mutableStateOf(TextFieldValue(""))}
    CountryList(navController = navController, state = textState)
}