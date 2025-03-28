package ru.itmo.data.mappers

import ru.itmo.chirper.domain.entity.Post
import ru.itmo.data.models.PostDto

class PostMapper : IMapper<PostDto, Post> {
    override fun map(dto: PostDto): Post {

        return Post(
            id = dto.id,
            username = "user${dto.userId}",
            title = dto.title,
            content = dto.body,
            date = "01-12-2025"
        )
    }
}