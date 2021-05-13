package com.mindera.flickergallery.data.repository

import com.mindera.flickergallery.data.model.Photo

interface PhotoRepository {
	suspend fun getPhotos(page: Int, perPage: Int): List<Photo>
}
