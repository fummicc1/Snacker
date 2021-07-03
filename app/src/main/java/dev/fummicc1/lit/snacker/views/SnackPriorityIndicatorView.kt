package dev.fummicc1.lit.snacker.views

import android.content.Context
import android.util.AttributeSet
import android.view.View

class SnackPriorityIndicatorView(context: Context, attr: AttributeSet?): View(context, attr) {

    var priority: Int = 0

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        val layoutParams = this.layoutParams
        val backgroundHeight = (parent as View).height
        layoutParams.height = (backgroundHeight.toFloat() * (priority.toFloat() / 5f)).toInt()
        this.layoutParams = layoutParams
    }
}