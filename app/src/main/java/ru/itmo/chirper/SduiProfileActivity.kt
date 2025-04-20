package ru.itmo.chirper

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.CornerFamily
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.json.JSONObject
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.itmo.chirper.state.ChirperState
import ru.itmo.chirper.viewmodel.SduiProfileViewModel
import ru.itmo.chirper.sdui.model.Component
import ru.itmo.chirper.sdui.model.position.Alignment
import ru.itmo.chirper.sdui.model.size.LayoutSize
import ru.itmo.chirper.sdui.parser.parseScreen

class SduiProfileActivity : AppCompatActivity() {

    private val viewModel: SduiProfileViewModel by viewModel()
    private lateinit var container: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sdui_profile)

        container = findViewById(R.id.llContainer)

        observeState()
        viewModel.getData("sduiprofile")
    }

    private fun observeState() {
        lifecycleScope.launch {
            viewModel.state.collectLatest { state ->
                when (state) {
                    is ChirperState.Loading -> {
                        container.removeAllViews()
                        showLoading()
                    }

                    is ChirperState.Success -> {
                        val fullMap = state.storage as? Map<String, Any>
                        if (fullMap == null) {
                            showError("Неправильный формат ответа")
                            return@collectLatest
                        }

                        val fullJson = try {
                            JSONObject(fullMap).toString()
                        } catch (e: Exception) {
                            showError("Ошибка сериализации данных")
                            return@collectLatest
                        }

                        renderScreen(fullJson)
                    }

                    is ChirperState.Error -> {
                        container.removeAllViews()
                        showError(state.message)
                    }
                }
            }
        }
    }

    private fun renderScreen(json: String) {
        val screen = parseScreen(json)
        title = screen.title
        container.removeAllViews()

        screen.components.forEach { comp ->
            when (comp) {
                is Component.Avatar -> addAvatar(comp)
                is Component.Text -> addText(comp)
                is Component.Card -> addCard(comp)
                is Component.Stats -> addStats(comp)
            }
        }
    }

    private fun addAvatar(comp: Component.Avatar) {
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
            .setAllCorners(CornerFamily.ROUNDED, size / 2f)
            .build()
        container.addView(iv)
    }

    private fun addText(comp: Component.Text) {
        val tv = TextView(this).apply {
            gravity = when (comp.layout.alignment) {
                Alignment.START  -> Gravity.START
                Alignment.CENTER -> Gravity.CENTER_HORIZONTAL
                Alignment.END    -> Gravity.END
            }

            layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
                comp.layout.margin.top?.let { topMargin = dp(it) }
                comp.layout.margin.bottom?.let { bottomMargin = dp(it) }
                comp.layout.margin.start?.let { marginStart = dp(it) }
                comp.layout.margin.end?.let { marginEnd = dp(it) }
            }

            text = comp.text
            setTextSize(TypedValue.COMPLEX_UNIT_SP, if (comp.style == "h2") 24f else 16f)
        }
        container.addView(tv)
    }

    private fun addCard(comp: Component.Card) {
        val wPx = toPx(comp.layout.width)
        val hPx = toPx(comp.layout.height)

        val lp = LinearLayout.LayoutParams(wPx, hPx).apply {
            comp.layout.margin.top?.let { topMargin = dp(it) }
            comp.layout.margin.bottom?.let { bottomMargin = dp(it) }
            comp.layout.margin.start?.let { marginStart = dp(it) }
            comp.layout.margin.end?.let { marginEnd = dp(it) }
        }

        val padding = comp.layout.padding
        val (pt, pb, ps, pe) = arrayOf(
            padding.top ?: padding.all ?: 0,
            padding.bottom ?: padding.all ?: 0,
            padding.start ?: padding.all ?: 0,
            padding.end ?: padding.all ?: 0
        ).map(::dp)

        val wrapper = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = lp
            background = GradientDrawable().apply {
                cornerRadius = dp(comp.style.cornerRadius ?: 8).toFloat()
                setColor(Color.parseColor(comp.style.backgroundColor ?: "#FFFFFF"))
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

    private fun addStats(comp: Component.Stats) {
        val row = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
        }

        val rowLp = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
            comp.layout.margin.top?.let { topMargin = dp(it) }
            comp.layout.margin.bottom?.let { bottomMargin = dp(it) }
            comp.layout.margin.start?.let { marginStart = dp(it) }
            comp.layout.margin.end?.let { marginEnd = dp(it) }
            gravity = when (comp.layout.alignment) {
                Alignment.START -> Gravity.START
                Alignment.CENTER -> Gravity.CENTER_HORIZONTAL
                Alignment.END -> Gravity.END
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

    private fun dp(dp: Int): Int =
        (dp * resources.displayMetrics.density).toInt()

    private fun toPx(size: LayoutSize): Int = when (size) {
        LayoutSize.MatchParent -> MATCH_PARENT
        LayoutSize.WrapContent -> WRAP_CONTENT
        is LayoutSize.Dp -> dp(size.value)
    }

    private fun showLoading() {
        Toast.makeText(this, "Загрузка...", Toast.LENGTH_SHORT).show()
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
