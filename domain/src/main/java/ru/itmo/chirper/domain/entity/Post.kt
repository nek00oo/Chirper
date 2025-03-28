package ru.itmo.chirper.domain.entity

data class Post(
    val id: Int,
    val username: String,
    val title: String,
    val content: String,
    val date: String
)