package com.example.qiitasampleproject.Util.Extension

import android.text.Html.fromHtml

fun String.removehtmlTag(): String{
    return  fromHtml(this).toString().replace("\n", "")
}

fun String.addText(text:String):String {
    return this + text
}


