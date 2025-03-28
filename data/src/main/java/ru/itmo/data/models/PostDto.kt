package ru.itmo.data.models

data class PostDto(
    val userId: Int,
    val id: Int,
    val title: String,
    val body: String
)
