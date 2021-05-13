package com.mindera.flickergallery.data.api

import com.mindera.flickergallery.BuildConfig
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FlickerAPI(private val baseUrl: String) {

	val webservice: FlickerService by lazy {
		Retrofit.Builder()
				.baseUrl(baseUrl)
				.addConverterFactory(GsonConverterFactory.create())
				.client(client)
				.build().create(FlickerService::class.java)
	}

	private val client: OkHttpClient by lazy {

		val builder = OkHttpClient.Builder().apply {
			if (BuildConfig.DEBUG){
				val logging = HttpLoggingInterceptor()
				logging.level = HttpLoggingInterceptor.Level.BODY
				addInterceptor(logging)
			}
		}
		builder.build()
	}

}
