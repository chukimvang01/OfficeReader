package com.example.officereader


sealed class NavigationItem(var route: String, var icon: Int, var title: String){
    object Pdf: NavigationItem("pdf",R.drawable.baseline_picture_as_pdf_24,"PDF")
    object Docx: NavigationItem("docx",R.drawable.baseline_description_24,"DOC")
    object Ppt: NavigationItem("ppt",R.drawable.baseline_slideshow_24,"PPT")
    object Txt: NavigationItem("txt",R.drawable.baseline_text_snippet_24,"TXT")
    object Excel: NavigationItem("excel",R.drawable.baseline_assignment_24,"EXCEL")
}