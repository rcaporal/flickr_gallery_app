package com.mindera.flickergallery.data.database

import com.mindera.flickergallery.data.model.Photo

interface PhotosDatabase {
	suspend fun getPhoto(id: String): Photo?
	suspend fun getAllPhotos(): List<Photo>
	suspend fun insert(photo: Photo)
	suspend fun delete(photo: Photo)
}
