package com.mindera.flickergallery.di


fun configureAppComponent(baseUrl: String) = listOf(
    networkModuleTest(baseUrl),
	emptyDatabaseModuleTest,
    repositoryModule
)
