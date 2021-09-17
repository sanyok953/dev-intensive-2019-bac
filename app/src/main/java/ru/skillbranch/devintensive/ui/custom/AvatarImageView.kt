package ru.skillbranch.devintensive.ui.custom

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.widget.ImageView
import ru.skillbranch.devintensive.utils.Utils

@SuppressLint("AppCompatCustomView")
class AvatarImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): ImageView(context, attrs, defStyleAttr) {
    private var avatarSize: Int = 0
    private var rect: Rect = Rect()
    private var pathR: Path = Path()
    private lateinit var paintText: Paint
    private lateinit var paintBorder: Paint
    private var borderWidth: Float = DEFAULT_BORDER_WIDTH
    //private var borderColor: Int = DEFAULT_BORDER_COLOR
    private var initials: String? = null
    private val bgColors = arrayOf(
        "#7BC862",
        "#E17076",
        "#FAA774",
        "#6EC9CB",
        "#65AADD",
        "#A695E7",
        "#EE7AAE"
    )

    fun setInitials(initials: String) {
        setImageDrawable(BitmapDrawable(resources, Utils.generateAvatar(context, 40, initials, 1)) )
    }

    companion object {
        private const val DEFAULT_BORDER_WIDTH = 2f
    }
}