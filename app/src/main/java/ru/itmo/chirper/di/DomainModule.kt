package ru.itmo.chirper.di

import org.koin.dsl.module
import ru.itmo.chirper.domain.usecase.GetPostsUseCase

val domainModule = module {

    factory<GetPostsUseCase> {
        GetPostsUseCase(postsRepository = get())
    }
}