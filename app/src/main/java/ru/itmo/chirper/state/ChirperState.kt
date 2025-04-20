package ru.itmo.chirper.state

import ru.itmo.chirper.domain.entity.Post

sealed class ChirperState {
    object Loading : ChirperState()
    data class Success(
        val posts: List<Post> = emptyList(),
        val storage: Map<String, Any> = emptyMap()
    ) : ChirperState()
    data class Error(val message: String) : ChirperState()
}