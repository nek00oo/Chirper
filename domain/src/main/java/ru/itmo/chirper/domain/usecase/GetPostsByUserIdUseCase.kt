package ru.itmo.chirper.domain.usecase

import ru.itmo.chirper.domain.entity.Post
import ru.itmo.chirper.domain.repository.IPostsRepository

class GetPostsByUserIdUseCase(private val postsRepository: IPostsRepository) {

    suspend fun getPostsByUserId(id: Int) : List<Post> {
        return postsRepository.getPostsByUserId(id)
    }
}