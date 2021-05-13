package com.mindera.flickergallery.di

import com.mindera.flickergallery.data.api.FlickerAPI
import com.mindera.flickergallery.util.NetworkHelper
import com.mindera.flickergallery.util.NetworkHelperImpl
import org.koin.dsl.module

fun networkModule(baseUrl: String) = module {
	factory { FlickerAPI(baseUrl) }
	single <NetworkHelper> { NetworkHelperImpl(context = get()) }
}
