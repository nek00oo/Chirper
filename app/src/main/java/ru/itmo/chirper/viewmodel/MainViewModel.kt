package ru.itmo.chirper.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.itmo.chirper.domain.entity.Post
import ru.itmo.chirper.domain.usecase.GetAllPostsUseCase
import ru.itmo.chirper.state.ChirperState

class MainViewModel(private val getAllPostsUseCase: GetAllPostsUseCase) : ViewModel() {
    private val _state = MutableStateFlow<ChirperState>(ChirperState.Loading)
    val state: StateFlow<ChirperState> = _state

    init {
        loadPosts()
    }

    private fun loadPosts() {
        viewModelScope.launch {
            try {
                val posts: List<Post> = getAllPostsUseCase.getPosts()
                _state.value = ChirperState.Success(posts)
            } catch (e: Exception) {
                e.printStackTrace()
                _state.value = ChirperState.Error("Ошибка загрузки постов: ${e.message}")
            }
        }
    }
}