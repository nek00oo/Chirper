package ru.itmo.chirper.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.itmo.chirper.viewmodel.MainViewModel
import ru.itmo.chirper.viewmodel.ProfileViewModel
import ru.itmo.chirper.viewmodel.SduiProfileViewModel

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

    viewModel<SduiProfileViewModel> {
        SduiProfileViewModel(getUseCase = get())
    }
}