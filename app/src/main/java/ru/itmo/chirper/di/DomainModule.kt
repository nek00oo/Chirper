package ru.itmo.chirper.di

import org.koin.dsl.module
import ru.itmo.chirper.domain.usecase.GetAllPostsUseCase
import ru.itmo.chirper.domain.usecase.GetPostsByUserIdUseCase

val domainModule = module {

    factory<GetAllPostsUseCase> {
        GetAllPostsUseCase(postsRepository = get())
    }

    factory<GetPostsByUserIdUseCase> {
        GetPostsByUserIdUseCase(postsRepository = get())
    }
}