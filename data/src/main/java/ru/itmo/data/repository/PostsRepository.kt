package ru.itmo.data.repository

import ru.itmo.chirper.domain.entity.Post
import ru.itmo.chirper.domain.repository.IPostsRepository
import ru.itmo.data.api.PostApi
import ru.itmo.data.mappers.IMapper
import ru.itmo.data.models.PostDto

class PostsRepository(private val api: PostApi, private val postMapper: IMapper<PostDto, Post>) : IPostsRepository {
    override suspend fun getPosts(): List<Post> {
        return api.getPosts().map { postMapper.map(it) }
    }
}