package ru.itmo.data.api

import retrofit2.http.GET
import retrofit2.http.Path
import ru.itmo.data.models.PostDto

interface PostApi {
    @GET("posts")
    suspend fun getPosts(): List<PostDto>

    @GET("user/{id}/posts")
    suspend fun getPostsByUserId(@Path("id") userId: Int): List<PostDto>
}