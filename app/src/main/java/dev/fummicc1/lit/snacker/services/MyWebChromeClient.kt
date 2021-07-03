package dev.fummicc1.lit.snacker.services

import android.webkit.WebChromeClient
import android.webkit.WebView

class MyWebChromeClient: WebChromeClient() {

    var handler: Handler? = null

    override fun onProgressChanged(view: WebView?, newProgress: Int) {
        super.onProgressChanged(view, newProgress)
        handler?.onProgressChanged(newProgress)
    }

    interface Handler {
        fun onProgressChanged(progress: Int)
    }
}