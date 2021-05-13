package com.mindera.flickergallery.di

import com.mindera.flickergallery.data.database.PhotosDatabase
import com.mindera.flickergallery.data.model.Photo
import org.koin.dsl.module

val emptyDatabaseModuleTest = module {
	single <PhotosDatabase> {
		object : PhotosDatabase {
			override suspend fun getPhoto(id: String): Photo? = null

			override suspend fun getAllPhotos(): List<Photo> = emptyList()

			override suspend fun insert(photo: Photo) {}

			override suspend fun delete(photo: Photo) {}
		}
	}
}

