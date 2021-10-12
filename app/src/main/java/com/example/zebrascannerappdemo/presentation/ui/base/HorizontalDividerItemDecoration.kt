package com.example.zebrascannerappdemo.presentation.ui.base

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt

class HorizontalDividerItemDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private val attrs = intArrayOf(android.R.attr.listDivider)
    private val bounds = Rect()

    private val divider: Drawable?

    init {
        val a: TypedArray = context.obtainStyledAttributes(attrs)
        divider = a.getDrawable(0)
        a.recycle()
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (parent.layoutManager == null || divider == null) {
            return
        }
        canvas.save()

        val left: Int
        val right: Int

        if (parent.clipToPadding) {
            left = parent.paddingLeft
            right = parent.width - parent.paddingRight
            canvas.clipRect(
                left, parent.paddingTop, right,
                parent.height - parent.paddingBottom
            )
        } else {
            left = 0
            right = parent.width
        }

        val childCount = if (parent.childCount > 0) parent.childCount - 1 else 0
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            parent.getDecoratedBoundsWithMargins(child, bounds)
            val bottom: Int = bounds.bottom + child.translationY.roundToInt()
            val top: Int = bottom - divider.intrinsicHeight
            divider.setBounds(left, top, right, bottom)
            divider.draw(canvas)
        }
        canvas.restore()
    }
}