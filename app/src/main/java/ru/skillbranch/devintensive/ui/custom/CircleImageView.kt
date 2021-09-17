package ru.skillbranch.devintensive.ui.custom

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.AttributeSet
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.annotation.Dimension
import androidx.core.content.ContextCompat
import ru.skillbranch.devintensive.App
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.extensions.dpToPx
import ru.skillbranch.devintensive.extensions.pxToDp

@SuppressLint("AppCompatCustomView")
class CircleImageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null,
                                                defStyleAttr:Int = 0): ImageView(context, attrs, defStyleAttr) {
    companion object {
        private const val BORDER_WIDTH = 2
        private const val BORDER_COLOR = Color.WHITE
    }

    private var borderWidth = BORDER_WIDTH
    private var borderColor = BORDER_COLOR

    private var btmBounds = RectF()
    private var borderBounds = RectF()


    private var shaderMatrix = Matrix()

    private var btmDraw = Paint(Paint.ANTI_ALIAS_FLAG)
    private var borderDraw = Paint(Paint.ANTI_ALIAS_FLAG)
    private var cvBitmap: Bitmap? = null
    private var cvBitmapShader: BitmapShader? = null
    private var isInit = false

    init {
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView)
            borderWidth = a.getDimensionPixelSize(R.styleable.CircleImageView_cv_borderWidth, BORDER_WIDTH).dpToPx()
            borderColor = a.getColor(R.styleable.CircleImageView_cv_borderColor, BORDER_COLOR)
            a.recycle()
        }

        borderDraw.strokeWidth = borderWidth.toFloat()
        borderDraw.color = borderColor
        borderDraw.style = Paint.Style.STROKE

        isInit = true
        setupBitmap()
    }

    fun getBorderWidth(): Int = borderWidth.pxToDp()

    fun setBorderWidth(@Dimension dp: Int) {
        borderWidth = dp.dpToPx()
        borderDraw.strokeWidth = borderWidth.toFloat()
        invalidate()
    }

    fun getBorderColor(): Int = borderColor

    fun setBorderColor(@ColorRes colorId: Int) {
        borderColor = ContextCompat.getColor(App.applicationContext(), colorId)
        borderDraw.color = borderColor
        invalidate()
    }
    fun setBorderColor(hex:String) {
        borderColor = Color.parseColor(hex)
        borderDraw.color = borderColor
        invalidate()
    }


    private fun getBitmapFromDrawable(drawable: Drawable?): Bitmap? {
        if (drawable == null)
            return null

        if (drawable is BitmapDrawable)
            return drawable.bitmap

        val bitmap =
            Bitmap.createBitmap( drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    fun setupBitmap() {
        if (!isInit)
            return

        cvBitmap = getBitmapFromDrawable(drawable)
        if (cvBitmap == null)
            return

        cvBitmapShader = BitmapShader(cvBitmap!!, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        btmDraw.setShader(cvBitmapShader)
        updateBitmapSize()
    }

    private fun updateBitmapSize() {
        if (cvBitmap == null)
            return

        val scale : Float
        val dx : Float
        val dy : Float

        if (cvBitmap!!.width < cvBitmap!!.height) {
            scale = btmBounds.width() / cvBitmap!!.width.toFloat()
            dx = btmBounds.left
            dy = btmBounds.top - (cvBitmap!!.height * scale * .5f) + btmBounds.width() *.5f
        } else {
            scale = btmBounds.height() / cvBitmap!!.height.toFloat()
            dx = btmBounds.left - (cvBitmap!!.width * scale * .5f) + btmBounds.width() *.5f
            dy = btmBounds.top
        }

        shaderMatrix.setScale(scale,scale)
        shaderMatrix.postTranslate(dx, dy)
        cvBitmapShader!!.setLocalMatrix(shaderMatrix)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        val halfStrokeWidth = borderDraw.strokeWidth *.5f
        setCircleBounds(btmBounds)
        borderBounds.set(btmBounds)
        borderBounds.inset(halfStrokeWidth, halfStrokeWidth)
        updateBitmapSize()
    }

    private fun setCircleBounds(bounds: RectF? ) {
        val contentWidth = (width - paddingLeft - paddingRight).toFloat()
        val contentHeight = (height - paddingTop - paddingBottom).toFloat()
        var left = paddingLeft.toFloat()
        var top = paddingTop.toFloat()

        if (contentWidth > contentHeight)
            left += (contentWidth - contentHeight) * .5f
        else
            top += (contentHeight - contentWidth) *.5f
        val diameter = Math.min(contentHeight, contentWidth)
        bounds!!.set(left, top, left+diameter, top + diameter)
    }

    override fun onDraw(canvas: Canvas?) {
        drawBitmap(canvas)
        drawStroke(canvas)
    }

    override fun setImageResource(resId: Int) {
        super.setImageResource(resId)
        setupBitmap()
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        setupBitmap()
    }

    override fun setImageBitmap(bm: Bitmap?) {
        super.setImageBitmap(bm)
        setupBitmap()
    }

    override fun setImageURI(uri: Uri?) {
        super.setImageURI(uri)
        setupBitmap()
    }


    private fun drawBitmap(canvas: Canvas?) {
        canvas!!.drawOval(btmBounds, btmDraw)
    }

    private fun drawStroke(canvas: Canvas?) {
        if (borderDraw.strokeWidth > .0)
            canvas!!.drawOval(borderBounds, borderDraw)
    }
}