package com.mindera.flickergallery.di

import com.mindera.flickergallery.data.repository.PhotoRepository
import com.mindera.flickergallery.data.repository.implementation.PhotoRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
	single <PhotoRepository> { PhotoRepositoryImpl(networkHelper = get(), api = get(), photoDatabase = get()) }
}
