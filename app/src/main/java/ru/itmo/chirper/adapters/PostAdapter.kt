package ru.itmo.chirper.adapters

import ru.itmo.chirper.views.PostCardView
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.itmo.chirper.domain.entity.Post

class PostAdapter : ListAdapter<Post, PostAdapter.PostViewHolder>(PostDiffCallback()) {

    class PostViewHolder(private val postCardView: PostCardView) : RecyclerView.ViewHolder(postCardView) {
        fun bind(post: Post) {
            postCardView.setUsername(post.username)
            postCardView.setTitle(post.title)
            postCardView.setContent(post.content)
            postCardView.setDate(post.date)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = PostCardView(parent.context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }


        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class PostDiffCallback : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem == newItem
        }
    }
}