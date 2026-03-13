package com.example.kmp_training.di

import com.example.kmp_training.data.NewsFeedRepository
import com.example.kmp_training.data.NewsFeedRepositoryImpl
import com.example.kmp_training.domain.GetNewsFeedUseCase
import com.example.kmp_training.screen.NewsFeedViewModel
import org.koin.dsl.module

private val repositoryModule = module {
    single<NewsFeedRepository> { NewsFeedRepositoryImpl() }
}

private val useCaseModule = module {
    single { GetNewsFeedUseCase(get()) }
}

private val viewModelModule = module {
    single { NewsFeedViewModel(get()) }
}

val appModule = listOf(
    repositoryModule,
    useCaseModule,
    viewModelModule
)
