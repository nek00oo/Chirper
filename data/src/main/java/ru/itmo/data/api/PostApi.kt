package ru.itmo.data.api

import retrofit2.http.GET
import ru.itmo.data.models.PostDto

interface PostApi {
    @GET("posts")
    suspend fun getPosts(): List<PostDto>
}