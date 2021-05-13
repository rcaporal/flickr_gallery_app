package com.mindera.flickergallery.di

import com.mindera.flickergallery.data.api.FlickerAPI
import com.mindera.flickergallery.util.NetworkHelper
import org.koin.dsl.module

fun networkModuleTest(baseUrl: String) = module {
	factory { FlickerAPI(baseUrl) }
	single <NetworkHelper> {
		object : NetworkHelper {
			override fun isOnline(): Boolean = true
		}
	}
}
