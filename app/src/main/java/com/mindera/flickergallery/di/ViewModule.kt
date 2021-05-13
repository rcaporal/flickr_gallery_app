package com.mindera.flickergallery.di

import com.mindera.flickergallery.view.view_model.PhotoListViewModel
import org.koin.dsl.module

val viewModule = module {
	factory { PhotoListViewModel(repository = get()) }
}
