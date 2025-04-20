package ru.itmo.chirper.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.itmo.chirper.viewmodel.MainViewModel
import ru.itmo.chirper.viewmodel.ProfileViewModel
import ru.itmo.chirper.viewmodel.StorageViewModel

val appModule = module {
    viewModel<MainViewModel> {
        MainViewModel(getAllPostsUseCase = get())
    }

    viewModel<ProfileViewModel> { (userId: Int) ->
        ProfileViewModel(
            getPostsByUserIdUseCase = get(),
            userId = userId
        )
    }

    viewModel<StorageViewModel> {
        StorageViewModel(getUseCase = get())
    }
}