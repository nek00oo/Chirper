package ru.itmo.chirper.di

import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import ru.itmo.chirper.domain.entity.Post
import ru.itmo.chirper.domain.repository.IPostsRepository
import ru.itmo.chirper.domain.repository.IStorageRepository
import ru.itmo.data.api.ApiClient
import ru.itmo.data.api.PostApi
import ru.itmo.data.api.StorageApi
import ru.itmo.data.api.StorageApiClient
import ru.itmo.data.mappers.IMapper
import ru.itmo.data.mappers.PostMapper
import ru.itmo.data.models.PostDto
import ru.itmo.data.repository.PostsRepository
import ru.itmo.data.repository.StorageRepository

val dataModule = module {
    single<IPostsRepository> {
        PostsRepository(api = get(), postMapper = get())
    }

    single<IStorageRepository> {
        StorageRepository(api = get())
    }

    single<IMapper<PostDto, Post>> {
        PostMapper()
    }

    single(named("PostRetrofit")) { ApiClient.retrofit }
    single(named("StorageRetrofit")) { StorageApiClient.retrofit }

    single<PostApi> { get<Retrofit>(named("PostRetrofit")).create(PostApi::class.java) }
    single<StorageApi> { get<Retrofit>(named("StorageRetrofit")).create(StorageApi::class.java) }
}