package com.mindera.flickergallery.data.database.implementation

import com.mindera.flickergallery.data.database.PhotoDAO
import com.mindera.flickergallery.data.database.PhotosDatabase
import com.mindera.flickergallery.data.model.Photo

class PhotoDatabaseImpl(private val photoDAO: PhotoDAO) : PhotosDatabase {

	override suspend fun getPhoto(id: String): Photo? {
		return photoDAO.getPhoto(id)
	}

	override suspend fun getAllPhotos(): List<Photo> {
		return photoDAO.getAllPhotos()
	}

	override suspend fun insert(photo: Photo) {
		return photoDAO.insert(photo)
	}

	override suspend fun delete(photo: Photo) {
		return photoDAO.delete(photo)
	}

}
