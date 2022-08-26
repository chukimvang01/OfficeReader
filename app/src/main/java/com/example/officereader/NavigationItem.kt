package com.example.officereader


sealed class NavigationItem(var route: String, var icon: Int, var title: String,val idx: Int){
    object Pdf: NavigationItem("pdf",R.drawable.baseline_picture_as_pdf_24,"PDF",0)
    object Docx: NavigationItem("docx",R.drawable.baseline_description_24,"DOC",1)
    object Txt: NavigationItem("txt",R.drawable.baseline_text_snippet_24,"TXT",2)
    object Ppt: NavigationItem("ppt",R.drawable.baseline_slideshow_24,"PPT",3)
    object Excel: NavigationItem("excel",R.drawable.baseline_assignment_24,"EXCEL",4)
    object Setting: NavigationItem("setting",R.drawable.baseline_settings_24,"SETTING",5)
}