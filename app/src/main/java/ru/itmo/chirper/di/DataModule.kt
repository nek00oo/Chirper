package ru.itmo.chirper.di

import org.koin.dsl.module
import retrofit2.Retrofit
import ru.itmo.chirper.domain.entity.Post
import ru.itmo.chirper.domain.repository.IPostsRepository
import ru.itmo.data.api.ApiClient
import ru.itmo.data.api.PostApi
import ru.itmo.data.mappers.IMapper
import ru.itmo.data.mappers.PostMapper
import ru.itmo.data.models.PostDto
import ru.itmo.data.repository.PostsRepository

val dataModule = module {

    single<IPostsRepository> {
        PostsRepository(api = get(), postMapper = get())
    }

    single<IMapper<PostDto, Post>> {
        PostMapper()
    }

    single<Retrofit> { ApiClient.retrofit }
    single<PostApi> { get<Retrofit>().create(PostApi::class.java) }
}