package ru.itmo.chirper.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.itmo.chirper.viewmodel.MainViewModel

val appModule = module {
    viewModel<MainViewModel> {
        MainViewModel(getPostsUseCase = get())
    }
}