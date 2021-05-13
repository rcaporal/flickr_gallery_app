package com.mindera.flickergallery

import android.app.Application
import com.mindera.flickergallery.di.applicationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class FlickerGalleryApplication : Application() {

	override fun onCreate() {
		super.onCreate()
		initTimberDebugLog()
		initDI()
	}

	private fun initDI() {
		startKoin {
			androidContext(this@FlickerGalleryApplication)
			modules(applicationModule)
		}
	}

	private fun initTimberDebugLog() {
		if (BuildConfig.DEBUG) {
			Timber.plant(Timber.DebugTree())
		}
	}

}
