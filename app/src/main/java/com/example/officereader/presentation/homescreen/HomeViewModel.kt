package com.example.officereader.presentation.homescreen

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.officereader.data.menu.NavigationItem
import com.example.officereader.data.menu.TabItem
import kotlinx.coroutines.CoroutineExceptionHandler
import java.util.*
import kotlin.collections.ArrayList


@OptIn(ExperimentalMaterialApi::class)
class HomeViewModel : ViewModel() {
    private val _state = mutableStateOf(HomeScreenState(mapOf(), listOf(),0))
    val state: State<HomeScreenState>
        get() = _state
    private val errorHandler = CoroutineExceptionHandler { _, ex ->
        ex.printStackTrace()
        _state.value = _state.value.copy(error = ex.message)
    }

    init {
        initSwipeScreem()
    }

    private fun initSwipeScreem() {
        val items = listOf(
            NavigationItem.Home,
            NavigationItem.Recent,
            NavigationItem.Favorite,
            NavigationItem.Setting
        )

        val tabs = listOf(
            TabItem.Pdf,
            TabItem.Docx,
            TabItem.Txt,
            TabItem.Ppt,
            TabItem.Excel
        )
        val rs: MutableMap<Int, NavigationItem> = mutableMapOf()
        items.forEach { item ->
            rs[item.idx] = item
        }
        _state.value = _state.value.copy(rs,tabs, 0)
    }

    fun updateActiveScreen(numberActive: Int) {
        _state.value = _state.value.copy(numberActive = numberActive)
    }


    fun swipeLeftUpdateState(): Int {
        return _state.value.numberActive + 1
    }

    fun swipeRightUpdateState(): Int {
        return _state.value.numberActive - 1
    }

    fun getItemScreenActive(key: Int): NavigationItem {
        var keyUpdate = key
        if (key > 5) {
            keyUpdate = 0
        } else if (key < 0) {
            keyUpdate = 5
        }
        _state.value.menuBottomBar.get(keyUpdate)?.let {
            return it
        }
        return NavigationItem.Home
    }

    fun getListOfCountries(): ArrayList<String> {
        val isoCountryCodes = Locale.getISOCountries()
        val countryListWithEmojis = ArrayList<String>()
        for (countrCode in isoCountryCodes){
            val locale = Locale("",countrCode)
            val countryName = locale.displayCountry
            val flagOffset = 0x1F1E6
            val asciiOffset = 0x41
            val firstChar = Character.codePointAt(countrCode,0) - asciiOffset + flagOffset
            val secondChar = Character.codePointAt(countrCode,1) - asciiOffset + flagOffset
            val flag = (String(Character.toChars(firstChar)) + String(Character.toChars(secondChar)))
            countryListWithEmojis.add("$countryName $flag")
        }
        return countryListWithEmojis
    }
}