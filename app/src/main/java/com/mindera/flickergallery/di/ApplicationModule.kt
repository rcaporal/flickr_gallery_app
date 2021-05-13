package com.mindera.flickergallery.di

val applicationModule = listOf(
	networkModule("https://api.flickr.com/"),
	databaseModule,
	repositoryModule,
	viewModule
)
