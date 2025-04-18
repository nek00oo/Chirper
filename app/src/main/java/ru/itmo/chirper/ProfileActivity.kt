package ru.itmo.chirper

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import ru.itmo.chirper.adapters.PostAdapter
import ru.itmo.chirper.databinding.ActivityProfileBinding
import ru.itmo.chirper.decoration.SpacingCardDecoration
import ru.itmo.chirper.domain.entity.Post
import ru.itmo.chirper.state.ChirperState
import ru.itmo.chirper.viewmodel.ProfileViewModel

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var viewModel: ProfileViewModel
    private lateinit var postAdapter: PostAdapter

    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val spacing = resources.getDimensionPixelSize(R.dimen.card_margin)
        binding.rvProfilePosts.addItemDecoration(SpacingCardDecoration(spacing))

        userId = intent.getIntExtra("USER_ID", -1).takeIf { it != -1 }
            ?: throw IllegalStateException("User ID must be provided!")

        viewModel = getViewModel(parameters = { parametersOf(userId) })

        binding.btnOpenSdui.setOnClickListener {
            val intent = Intent(this, SduiProfileActivity::class.java)
            startActivity(intent)
        }

        setupRecyclerView()
        setupObservers()
    }

    private fun setupRecyclerView() {
        postAdapter = PostAdapter()
        binding.rvProfilePosts.apply {
            layoutManager = LinearLayoutManager(this@ProfileActivity)
            adapter = postAdapter
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    when (state) {
                        is ChirperState.Loading -> showLoading()
                        is ChirperState.Success -> {
                            showPosts(state.posts)
                            binding.tvProfileUsername.text = "user$userId"
                        }
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
        postAdapter.submitList(posts)
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}