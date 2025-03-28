package ru.itmo.chirper.domain.usecase

import ru.itmo.chirper.domain.entity.Post
import ru.itmo.chirper.domain.repository.IPostsRepository

class GetPostsUseCase(private val postsRepository: IPostsRepository) {

    suspend fun getPosts(): List<Post> {
        return postsRepository.getPosts()
    }
}