package dev.fummicc1.lit.snacker.views

import android.content.Context
import android.util.AttributeSet
import android.webkit.WebView
import androidx.lifecycle.MutableLiveData

class ObservableScrollingWebView(context: Context, attr: AttributeSet?) : WebView(context, attr) {

    interface EventListener {
        val onScrollContentIsAlmostRead: MutableLiveData<Boolean>
        val onScrollingToDirectionChanged: MutableLiveData<Boolean>
        // from 0 to 100
        val onProgressChanged: MutableLiveData<Int>
    }

    var listener: EventListener? = null

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)

        listener?.apply {
            val contentHeight = computeVerticalScrollRange()
            val remaining = contentHeight - t - height
            val progress = ((t + height).toFloat() / contentHeight.toFloat() * 100 ).toInt()
            onProgressChanged.postValue(progress)
            onScrollContentIsAlmostRead.postValue(remaining == 0)

            // 静止していたら何もしない
            if (t == oldt) return

            onScrollingToDirectionChanged.postValue(t - oldt < 0)
        }
    }
}