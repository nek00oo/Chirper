package ru.itmo.chirper.domain.repository

import ru.itmo.chirper.domain.entity.Post

interface IPostsRepository {
    suspend fun getPosts(): List<Post>

    suspend fun getPostsByUserId(id: Int): List<Post>
}