package ru.itmo.chirper

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.CornerFamily
import ru.itmo.chirper.sdui.model.Component
import ru.itmo.chirper.sdui.model.position.Alignment
import ru.itmo.chirper.sdui.model.size.LayoutSize
import ru.itmo.chirper.sdui.parser.parseScreen

class SduiProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sdui_profile)

        val json = assets.open("profile_screen.json")
            .bufferedReader().use { it.readText() }
        val screen = parseScreen(json)

        title = screen.title

        val container = findViewById<LinearLayout>(R.id.llContainer)

        fun toPx(size: LayoutSize): Int = when(size) {
            LayoutSize.MatchParent -> MATCH_PARENT
            LayoutSize.WrapContent -> WRAP_CONTENT
            is LayoutSize.Dp       -> dp(size.value)
        }

        screen.components.forEach { comp ->
            when (comp) {
                is Component.Avatar -> {
                    val iv = ShapeableImageView(this)

                    val size = resources.getDimensionPixelSize(R.dimen.avatar_size)
                    val lp = LinearLayout.LayoutParams(size, size).apply {
                        comp.layout.margin.top?.let    { topMargin    = dp(it) }
                        comp.layout.margin.bottom?.let { bottomMargin = dp(it) }
                        comp.layout.margin.start?.let  { marginStart  = dp(it) }
                        comp.layout.margin.end?.let    { marginEnd    = dp(it) }
                        gravity = when (comp.layout.alignment) {
                            Alignment.START  -> Gravity.START
                            Alignment.CENTER -> Gravity.CENTER_HORIZONTAL
                            Alignment.END    -> Gravity.END
                        }
                    }
                    iv.layoutParams = lp

                    iv.setImageResource(R.drawable.ic_profile)
                    iv.scaleType = ImageView.ScaleType.CENTER_CROP
                    iv.shapeAppearanceModel = iv.shapeAppearanceModel.toBuilder()
                        .setAllCorners(
                            CornerFamily.ROUNDED,
                            size / 2f
                        )
                        .build()

                    container.addView(iv)
                }

                is Component.Text -> {
                    val tv = TextView(this).apply {
                        gravity = when (comp.layout.alignment) {
                            Alignment.START  -> Gravity.START
                            Alignment.CENTER -> Gravity.CENTER_HORIZONTAL
                            Alignment.END    -> Gravity.END
                        }

                        layoutParams = LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            WRAP_CONTENT
                        ).apply {
                            comp.layout.margin.top   ?.let { topMargin    = dp(it) }
                            comp.layout.margin.bottom?.let { bottomMargin = dp(it) }
                            comp.layout.margin.start ?.let { marginStart  = dp(it) }
                            comp.layout.margin.end   ?.let { marginEnd    = dp(it) }
                        }

                        text = comp.text
                        if (comp.style == "h2") {
                            setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f)
                        } else {
                            setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
                        }
                    }
                    container.addView(tv)

                }

                is Component.Card -> {
                    val wPx = toPx(comp.layout.width)
                    val hPx = toPx(comp.layout.height)

                    val lp = LinearLayout.LayoutParams(wPx, hPx).apply {
                        comp.layout.margin.top   ?.let { topMargin    = dp(it) }
                        comp.layout.margin.bottom?.let { bottomMargin = dp(it) }
                        comp.layout.margin.start ?.let { marginStart  = dp(it) }
                        comp.layout.margin.end   ?.let { marginEnd    = dp(it) }
                    }

                    val padding = comp.layout.padding
                    val (pt, pb, ps, pe) = arrayOf(
                        padding.top    ?: padding.all ?: 0,
                        padding.bottom ?: padding.all ?: 0,
                        padding.start  ?: padding.all ?: 0,
                        padding.end    ?: padding.all ?: 0
                    ).map(::dp)

                    val wrapper = LinearLayout(this).apply {
                        orientation  = LinearLayout.VERTICAL
                        layoutParams = lp

                        val bgColor = comp.style.backgroundColor ?: "#FFFFFF"
                        val radius  = dp(comp.style.cornerRadius ?: 8)
                        background = GradientDrawable().apply {
                            cornerRadius = radius.toFloat()
                            setColor(Color.parseColor(bgColor))
                        }

                        setPadding(ps, pt, pe, pb)
                    }

                    val tv = TextView(this).apply {
                        text = comp.text
                        setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
                    }
                    wrapper.addView(tv)
                    container.addView(wrapper)
                }

                is Component.Stats -> {
                    val row = LinearLayout(this).apply {
                        orientation = LinearLayout.HORIZONTAL
                    }

                    val rowLp = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        WRAP_CONTENT
                    ).apply {
                        comp.layout.margin.top?.let    { topMargin    = dp(it) }
                        comp.layout.margin.bottom?.let { bottomMargin = dp(it) }
                        comp.layout.margin.start?.let  { marginStart  = dp(it) }
                        comp.layout.margin.end?.let    { marginEnd    = dp(it) }
                        gravity = when (comp.layout.alignment) {
                            Alignment.START  -> Gravity.START
                            Alignment.CENTER -> Gravity.CENTER_HORIZONTAL
                            Alignment.END    -> Gravity.END
                        }
                    }
                    row.layoutParams = rowLp

                    comp.items.forEach { item ->
                        val col = LinearLayout(this).apply {
                            orientation = LinearLayout.VERTICAL
                            layoutParams = LinearLayout.LayoutParams(0, WRAP_CONTENT, 1f)
                        }
                        col.addView(TextView(this).apply {
                            text = item.value
                            setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
                            gravity = Gravity.CENTER
                        })
                        col.addView(TextView(this).apply {
                            text = item.label
                            setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
                            gravity = Gravity.CENTER
                        })
                        row.addView(col)
                    }
                    container.addView(row)
                }
            }
        }
    }

    private fun dp(dp: Int): Int =
        (dp * resources.displayMetrics.density).toInt()

}
