package com.domonz.PrashantAdvait.utils

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.core.widget.NestedScrollView

class CustomNestedScrollView : NestedScrollView {

    private var lastY = 0f
    private var isTopOverScrolled = false
    private var onOverScrollListener: (() -> Unit)? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                lastY = ev.y
                isTopOverScrolled = !canScrollVertically(-1)
            }
            MotionEvent.ACTION_MOVE -> {
                val deltaY = ev.y - lastY
                if (deltaY > 0 && isTopOverScrolled) {
                    // Overscrolling at the top
                    onOverScrollListener?.invoke()
                }
            }
        }
        return super.onTouchEvent(ev)
    }

    fun setOnOverScrollListener(listener: () -> Unit) {
        onOverScrollListener = listener
    }
}
