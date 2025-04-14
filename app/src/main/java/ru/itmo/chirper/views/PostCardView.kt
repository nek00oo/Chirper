package ru.itmo.chirper.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.cardview.widget.CardView
import ru.itmo.chirper.R
import ru.itmo.chirper.databinding.PostCardContentBinding

class PostCardView(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.postCardStyle
) : CardView(context, attrs, defStyleAttr) {

    private val binding: PostCardContentBinding

    init {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.post_card_content, this, false)
        addView(view)
        binding = PostCardContentBinding.bind(view)

        setCardBackgroundColor(context.getColor(R.color.card_background))
        radius = resources.getDimension(R.dimen.card_corner_radius)
        elevation = resources.getDimension(R.dimen.card_elevation)

        context.obtainStyledAttributes(attrs, R.styleable.PostCardView).run {
            try {
                binding.tvPostTitle.textSize = getDimension(
                    R.styleable.PostCardView_titleTextSize,
                    resources.getDimension(R.dimen.text_size_title)
                ) / resources.displayMetrics.scaledDensity

                binding.tvPostContent.maxLines = getInt(
                    R.styleable.PostCardView_contentMaxLines,
                    5
                )

                binding.tvPostDate.visibility = if (getBoolean(
                        R.styleable.PostCardView_showDate,
                        true
                    )) VISIBLE else GONE

            } finally {
                recycle()
            }
        }
    }

    fun setUsername(username: String?) {
        binding.tvUsername.text = username ?: "Unknown"
    }

    fun setTitle(title: String?) {
        binding.tvPostTitle.text = title ?: ""
    }

    fun setContent(content: String?) {
        binding.tvPostContent.text = content ?: ""
    }

    fun setDate(date: String?) {
        binding.tvPostDate.text = date ?: ""
    }
}
