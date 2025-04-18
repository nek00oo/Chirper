package ru.itmo.chirper

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.itmo.chirper.adapters.PostAdapter
import ru.itmo.chirper.databinding.ActivityMainBinding
import ru.itmo.chirper.decoration.SpacingCardDecoration
import ru.itmo.chirper.domain.entity.Post
import ru.itmo.chirper.state.ChirperState
import ru.itmo.chirper.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var recyclerView: RecyclerView
    private val postAdapter = PostAdapter()
    private val vm: MainViewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.rvPosts
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = postAdapter

        val spacing = resources.getDimensionPixelSize(R.dimen.card_margin)
        binding.rvPosts.addItemDecoration(SpacingCardDecoration(spacing))

        binding.fabProfile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java).apply {
                putExtra("USER_ID", 2)
            }

            val options = ActivityOptions.makeCustomAnimation(
                this,
                R.anim.slide_in_right,
                R.anim.slide_out_left
            ).toBundle()

            startActivity(intent, options)
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.state.collect { state ->
                    when (state) {
                        is ChirperState.Loading -> showLoading()
                        is ChirperState.Success -> showPosts(state.posts)
                        is ChirperState.Error -> showError(state.message)
                    }
                }
            }
        }
    }

    private fun showLoading() {
        Toast.makeText(this, "Загрузка постов...", Toast.LENGTH_SHORT).show()
    }

    private fun showPosts(posts: List<Post>) {
        if (posts.isEmpty()) {
            Toast.makeText(this, "Нет постов для отображения", Toast.LENGTH_SHORT).show()
            return
        }

        postAdapter.submitList(posts)
        recyclerView.smoothScrollToPosition(0)
    }

    private fun showError(message: String) {
        Toast.makeText(this, "Ошибка: $message", Toast.LENGTH_LONG).show()
    }
}