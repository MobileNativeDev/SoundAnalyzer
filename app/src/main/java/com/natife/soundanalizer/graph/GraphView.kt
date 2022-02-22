package com.natife.soundanalizer.graph

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.natife.soundanalizer.R

val Int.dp: Float
    get() = Resources.getSystem().displayMetrics.density * this

private val DEFAULT_POINT_STEP = 4.dp
private const val DEFAULT_SCALE = 0.1f
private val DEFAULT_STROKE_WIDTH = 2.dp
private const val DEFAULT_COLOR = Color.RED

class GraphView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {

    private val values = mutableListOf<Float>()
    private var pointStep: Float = DEFAULT_POINT_STEP
    private var scale = DEFAULT_SCALE
    private val path = Path()
    private val paint = Paint().apply {
        style = Paint.Style.STROKE
        color = DEFAULT_COLOR
        strokeWidth = DEFAULT_STROKE_WIDTH
        isAntiAlias = true
    }
    private val graphBounds = RectF()

    init {
        try {
            context.obtainStyledAttributes(attrs, R.styleable.GraphView).apply {
                paint.color = getColor(R.styleable.GraphView_graphColor, DEFAULT_COLOR)
                paint.strokeWidth = getDimension(
                    R.styleable.GraphView_strokeWidth, DEFAULT_STROKE_WIDTH
                )
                pointStep = getDimension(R.styleable.GraphView_pointStep, DEFAULT_POINT_STEP)
                recycle()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        graphBounds.set(
            paddingStart.toFloat(),
            paddingTop.toFloat(),
            (extractSize(widthMeasureSpec) - paddingEnd).toFloat(),
            (extractSize(heightMeasureSpec) - paddingBottom).toFloat()
        )
    }

    private fun extractSize(measureSpec: Int): Int {
        return MeasureSpec.getSize(measureSpec)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        values.forEachIndexed { index, value ->
            if (index == 0) {
                path.moveTo(graphBounds.right - values.size * pointStep, graphBounds.bottom - value)
            } else {
                val x = graphBounds.right - (values.size - index) * pointStep
                val y = graphBounds.bottom - value
                path.lineTo(x, y)
            }
        }
        canvas?.drawPath(path, paint)
        path.reset()
    }

    fun addItem(value: Float) {
        values.add(value * scale)
        if (values.size * pointStep > graphBounds.right) {
            values.removeAt(0)
        }
        invalidate()
    }

    fun setScale(scale: Float) {
        val newValues = values.map { it / this.scale }.map { it * scale }
        this.scale = scale
        values.clear()
        values.addAll(newValues)
        invalidate()
    }

}