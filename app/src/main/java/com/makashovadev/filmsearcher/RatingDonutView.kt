package com.makashovadev.filmsearcher

import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View


// Кастомный прогресс-бар, который будет показывать, какой рейтинг у фильма
class RatingDonutView @JvmOverloads constructor(
    context: Context, attributeSet: AttributeSet? = null) :
    //мы наследуемся от класса View и прокидываем в него Context, а также атрибуты, которые мы будем устанавливать из XML.
    View(context, attributeSet) {
    //Овал для рисования сегментов прогресс бара
    private val oval = RectF()
    //Координаты центра View, а также Radius
    private var radius: Float = 0f
    private var centerX: Float = 0f
    private var centerY: Float = 0f
    //Толщина линии прогресса
    private var stroke = 10f
    //Значение прогресса от 0 - 100
    private var progress = 50
    //Значения размера текста внутри кольца
    private var scaleSize = 60f
    //Краски для наших фигур
    private lateinit var strokePaint: Paint
    private lateinit var digitPaint: Paint
    private lateinit var circlePaint: Paint

    private lateinit var bitmap: Bitmap
    // canvas для статичной картинки
    private lateinit var staticCanvas: Canvas
    private var isStaticPictureDrawn = false
    // текущее значение угла для анимации рейтинга
    private var currentPercentage = 0f


    init {
        //Получаем атрибуты и устанавливаем их в соответствующие поля
        val a =
            context.theme.obtainStyledAttributes(attributeSet, R.styleable.RatingDonutView, 0, 0)
        try {
            stroke = a.getFloat(
                R.styleable.RatingDonutView_stroke, stroke)
            progress = a.getInt(R.styleable.RatingDonutView_progress, progress)
        } finally {
            a.recycle()
        }
        //Инициализируем первоначальные краски
        initPaint()
    }

    fun setProgress(pr: Int,  st: Float = 10f) {
        //Кладем новое значение в наше поле класса
        progress = pr
        stroke = st
        //Создаем краски с новыми цветами
        initPaint()
        //вызываем перерисовку View
        invalidate()
        // начало анимации после обновления рейтинга
        animateArc()
    }

    // анимация с использованием ValueAnimator
    private fun animateArc() {
        val valuesHolder = PropertyValuesHolder.ofFloat(PERCENTAGE_VALUE_HOLDER, 0f,
            convertProgressToDegrees(progress))
        val animator = ValueAnimator().apply {
            setValues(valuesHolder)
            duration = ANIMATION_TIME
            addUpdateListener {
                currentPercentage = it.animatedValue as Float
                invalidate()
            }
        }
        animator.start()
    }


    // расчет размеров
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)

        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val chosenWidth = chooseDimension(widthMode, widthSize)
        val chosenHeight = chooseDimension(heightMode, heightSize)

        val minSide = Math.min(chosenWidth, chosenHeight)
        centerX = minSide.div(2f)
        centerY = minSide.div(2f)

        setMeasuredDimension(minSide, minSide)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        radius = if (width > height) {
            height.div(2f)
        } else {
            width.div(2f)
        }
    }

    private fun chooseDimension(mode: Int, size: Int) =
        when (mode) {
            MeasureSpec.AT_MOST, MeasureSpec.EXACTLY -> size
            else -> 300
        }

    override fun onDraw(canvas: Canvas) {
        //Рисуем кольцо и задний фон
        drawRating(canvas)
        //Рисуем цифры
        drawText(canvas)
    }

    //  метод, который будет рисовать кольцо рейтинга
    private fun drawRating(canvas: Canvas) {
        // отрисована ли у нас статичная картинка
        if (!isStaticPictureDrawn) {
            drawStaticPicture()
        }
        canvas.drawBitmap(bitmap, centerX - radius, centerY - radius, null)
        //Сохраняем канвас
        canvas.save()
        //Перемещаем нулевые координаты канваса в центр, так проще рисовать все круглое
        canvas.translate(centerX, centerY)
        // ДИНАМИЧЕСКАЯ ЧАСТЬ
        //Рисуем "арки", из них и будет состоять наше кольцо + у нас тут специальный метод
        drawInnerArc(canvas)
        //Восстанавливаем канвас
        canvas.restore()
        // перерисовка каждые 0.5с
        //postInvalidateDelayed(500)
    }

    // рисуем статичную часть
    private fun drawStaticPicture() {
        // создается битмап
        bitmap = Bitmap.createBitmap(
            (centerX * 2).toInt(), // ширина
            (centerY * 2).toInt(), // высота
            Bitmap.Config.ARGB_8888 // конфигурация (как рендерится картинка)
        )
        // canvas созданный на основании битмапа
        // один раз отрисовываем статичную картинку
        // и она остается в переменной bitmap
        staticCanvas = Canvas(bitmap)
        drawCircle(staticCanvas)
        isStaticPictureDrawn = true
    }

    private fun drawCircle(canvas: Canvas) {
        canvas.save()
        canvas.translate(centerX, centerY)
        staticCanvas.drawCircle(0f, 0f, radius, circlePaint)
        canvas.restore()
    }


    // Анимированная отрисовка рейтинга
    private fun drawInnerArc(canvas: Canvas) {
        //Здесь мы можем регулировать размер нашего кольца
        val scale = radius * 0.8f
        //Устанавливаем размеры под наш овал
        oval.set(0f - scale, 0f - scale, scale , scale)
        canvas.drawArc(oval, -90f,  currentPercentage, false, strokePaint)
    }

    //  метод для отрисовки текста
    private fun drawText(canvas: Canvas) {
        //Форматируем текст, чтобы мы выводили дробное число с одной цифрой после точки
        val message = String.format("%.1f", progress / 10f)
        //Получаем ширину и высоту текста, чтобы компенсировать их при отрисовке, чтобы текст был
        //точно в центре
        val widths = FloatArray(message.length)
        digitPaint.getTextWidths(message, widths)
        var advance = 0f
        for (width in widths) advance += width
        //Рисуем наш текст
        canvas.drawText(message, centerX - advance / 2, centerY  + advance / 4, digitPaint)
    }

    private fun convertProgressToDegrees(progress: Int): Float = progress *
            (ARC_FULL_ROTATION_DEGREE / PERCENTAGE_DIVIDER)

    // инициализация полей под краски
    private fun initPaint() {
        //Краска для колец
        strokePaint = Paint().apply {
            style = Paint.Style.STROKE
            //Сюда кладем значение из поля класса, потому как у нас краски будут видоизменяться
            strokeWidth = stroke
            //Цвет мы тоже будем получать в специальном методе, потому что в зависимости от рейтинга
            //мы будем менять цвет нашего кольца
            color = getPaintColor(progress)
            isAntiAlias = true
        }
        //Краска для цифр
        digitPaint = Paint().apply {
            style = Paint.Style.FILL_AND_STROKE
            strokeWidth = 2f
            setShadowLayer(5f, 0f, 0f, Color.DKGRAY)
            textSize = scaleSize
            typeface = Typeface.SANS_SERIF
            color = getPaintColor(progress)
            isAntiAlias = true
        }
        //Краска для заднего фона
        circlePaint = Paint().apply {
            style = Paint.Style.FILL
            color = Color.DKGRAY
        }
    }

    private fun getPaintColor(progress: Int): Int = when(progress) {
        in 0 .. 25 -> Color.parseColor("#e84258")
        in 26 .. 50 -> Color.parseColor("#fd8060")
        in 51 .. 75 -> Color.parseColor("#fee191")
        else -> Color.parseColor("#b0d8a4")
    }


    // константы для анимации
    companion object {
        const val ANIMATION_TIME = 10000L
        const val ARC_FULL_ROTATION_DEGREE = 360f
        const val PERCENTAGE_DIVIDER = 100.0f
        const val PERCENTAGE_VALUE_HOLDER = "progress"
    }

    }