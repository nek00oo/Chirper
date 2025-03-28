package ru.itmo.chirper.domain.repository

import ru.itmo.chirper.domain.entity.Post

interface IPostsRepository {
    suspend fun getPosts(): List<Post>
}