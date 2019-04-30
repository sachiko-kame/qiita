package com.example.qiitasampleproject.Util.Extension

import android.os.Build
import android.text.Html



fun String.removehtmlTag(): String{
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY).toString().replace("\n", "")
    } else {
        Html.fromHtml(this).toString().replace("\n", "")
    }
}

fun String.addText(text:String):String {
    return this + text
}
