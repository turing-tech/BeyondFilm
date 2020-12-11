package com.wynneplaga.beyondfilm

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

class SquareImageView(
    context: Context,
    attributeSet: AttributeSet?,
    defStyleAttr: Int
): AppCompatImageView(context, attributeSet, defStyleAttr) {

    constructor(context: Context): this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?): this(context, attributeSet, 0)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int)  {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        setMeasuredDimension(measuredWidth, measuredWidth);
    }

}