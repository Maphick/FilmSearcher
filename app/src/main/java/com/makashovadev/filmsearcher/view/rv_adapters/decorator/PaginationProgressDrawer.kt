package com.makashovadev.filmsearcher.view.rv_adapters.decorator

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.makashovadev.filmsearcher.R


private const val RADIUS = 30f
private const val MARGIN_BOTTOM = 40f

internal object PaginationProgressDrawer {

    private val oval = RectF()
    private var sweepStep = 4
    private var startAngle = 0f
    private var sweepAngle = 10f
    private var isIncrement = true
    private var shouldFreeze = false
    private var freezeCount = 0
    private val speed: Float
        get() = when {
            sweepAngle >= 0f && sweepAngle <= 20f -> 1.5f
            sweepAngle > 20f && sweepAngle <= 40f -> 1.6f
            sweepAngle > 40f && sweepAngle <= 60f -> 2.2f
            sweepAngle > 60f && sweepAngle <= 80f -> 2.4f
            sweepAngle > 80f && sweepAngle <= 100f -> 2.5f
            sweepAngle > 100f && sweepAngle <= 180f -> 2.6f
            sweepAngle > 180f && sweepAngle <= 200f -> 2.4f
            sweepAngle > 200f && sweepAngle <= 220f -> 2.2f
            sweepAngle > 220f && sweepAngle <= 240f -> 2.1f
            sweepAngle > 240f && sweepAngle <= 260f -> 1.6f
            sweepAngle > 260f && sweepAngle <= 280f -> 1.5f
            else -> 1f
        }


    private val paint = Paint().apply {
        strokeWidth = 18f//.dp.toFloat()
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        isAntiAlias = true
    }

    fun drawSpinner(recyclerView: RecyclerView, view: View?, canvas: Canvas) {
        if (view == null) return

        val coorX = (view.right / 2).toFloat()
        val coorY = view.bottom.toFloat() + MARGIN_BOTTOM
        val ovalDiameter = RADIUS * 2

        setPaintColor(view.context)
        oval.set(coorX, coorY, coorX + ovalDiameter, coorY + ovalDiameter)
        calculateStartAngle()
        calculateSweepAngle()
        canvas.drawArc(oval, startAngle, sweepAngle, false, paint)
        recyclerView.invalidate()
    }

    private fun setPaintColor(context: Context) {
        if (paint.color <= 1) {
            paint.color = Color.MAGENTA
        }
    }

    private fun calculateStartAngle() {

        val amountDegrees = 360f
        startAngle += when (isIncrement) {
            true -> sweepStep
            false -> sweepStep * 2
        } * speed

        startAngle %= amountDegrees
    }

    private fun calculateSweepAngle() {
        if (shouldFreeze && freezeCount < 2) {
            freezeCount++
            return
        }

        when (isIncrement) {
            true -> sweepAngle += sweepStep * speed
            else -> sweepAngle -= sweepStep * speed
        }

        configureAdditionalParams()
    }

    private fun configureAdditionalParams() {
        val minAngle = 10
        val maxAngle = 280
        freezeCount = 0
        shouldFreeze = false

        when {
            sweepAngle >= maxAngle -> {
                isIncrement = false
                shouldFreeze = true
            }
            sweepAngle <= minAngle -> {
                isIncrement = true
                shouldFreeze = true
            }
        }
    }
}