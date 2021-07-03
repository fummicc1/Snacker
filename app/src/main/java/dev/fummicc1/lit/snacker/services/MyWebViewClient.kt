package dev.fummicc1.lit.snacker.services

import android.graphics.Bitmap
import android.util.Log
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

class MyWebViewClient: WebViewClient() {

    var listener: Listener? = null

    private var currentWebPageUrl: String? = null

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        request?.url?.toString()?.let {
            listener?.onStartLoadingPage(it)
        }
        return false
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        val title = view?.title
        if (url != null && url != currentWebPageUrl) {
            listener?.onUpdatePage(url, title)
            currentWebPageUrl = url
        }
    }

    override fun onReceivedError(
        view: WebView?,
        request: WebResourceRequest?,
        error: WebResourceError?
    ) {
        super.onReceivedError(view, request, error)
        Log.e("MyWebViewClient", error.toString())
    }

    interface Listener {
        fun onStartLoadingPage(url: String)
        fun onUpdatePage(url: String, title: String?)
    }
}