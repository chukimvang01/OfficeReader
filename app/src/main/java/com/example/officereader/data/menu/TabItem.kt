package com.example.officereader.data.menu

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.Composable
import com.example.officereader.*
import kotlinx.coroutines.CoroutineScope

typealias ComposableFun = @Composable () -> Unit
sealed class TabItem(var icon: Int, var title: String, var screen: ComposableFun) {
    @ExperimentalMaterialApi
    object Pdf : TabItem(R.drawable.baseline_picture_as_pdf_24, "Pdf", { PdfScreen() })
    object Docx : TabItem(R.drawable.baseline_description_24, "Docx", { DocxScreen() })
    object Txt : TabItem(R.drawable.baseline_text_snippet_24, "Txt", { TxtScreen() })
    object Ppt : TabItem(R.drawable.baseline_slideshow_24, "Ppt", { PPTScreen() })
    object Excel : TabItem(R.drawable.baseline_assignment_24, "Excel", { ExcelScreen() })
}