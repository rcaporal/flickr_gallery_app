package com.mindera.flickergallery.data.repository.implementation

import com.mindera.flickergallery.data.api.FlickerAPI
import com.mindera.flickergallery.data.database.PhotosDatabase
import com.mindera.flickergallery.data.database.implementation.PhotoRoomDatabase
import com.mindera.flickergallery.data.model.Photo
import com.mindera.flickergallery.data.repository.PhotoRepository
import com.mindera.flickergallery.util.NetworkHelper
import com.mindera.flickergallery.util.ThereIsNoPhotosOnDatabase
import timber.log.Timber

const val TAG_PHOTO_REPO = "TAG_PHOTO_REPO"

class PhotoRepositoryImpl(
	private val networkHelper: NetworkHelper,
	private val api: FlickerAPI,
	private val photoDatabase: PhotosDatabase
	) : PhotoRepository {

	override suspend fun getPhotos(page: Int, perPage: Int): List<Photo> {
		val result: MutableList<Photo> = mutableListOf()
		Timber.tag(TAG_PHOTO_REPO).d("request page [$page]")

		when {
			networkHelper.isOnline() -> {
				Timber.tag(TAG_PHOTO_REPO).d("ONLINE => get from server")
				result.addAll(getPhotosPageFromServer(page, perPage))
			}
			isInitialRequest(page) -> {
				Timber.tag(TAG_PHOTO_REPO).d("OFFLINE => get from database")
				result.addAll(getPhotosFromDatabase())
			}
			else -> {
				throw (ThereIsNoPhotosOnDatabase())
			}
		}

		return result
	}

	private fun isInitialRequest(page: Int) = page == 1

	private suspend fun getPhotosFromDatabase(): MutableList<Photo> {
		val result: MutableList<Photo> = mutableListOf()
		result.addAll(photoDatabase.getAllPhotos())
		if (result.isEmpty()) throw (ThereIsNoPhotosOnDatabase())
		return result
	}

	private suspend fun getPhotosPageFromServer(
		page: Int,
		perPage: Int
	): MutableList<Photo> {
		val result: MutableList<Photo> = mutableListOf()
		api.webservice.getPhotosIds(page, perPage).photos.photo.forEach { photoInfo ->
			result.add(getPhotoById(photoInfo.id))
		}
		return result
	}

	private suspend fun getPhotoById(id: String): Photo {
		return getPhotoByIdFromDatabase(id) ?: getPhotoByIdFromServer(id)
	}

	private suspend fun getPhotoByIdFromDatabase(id: String): Photo? {
		val photo: Photo? = photoDatabase.getPhoto(id)
		if (photo != null) Timber.tag(TAG_PHOTO_REPO).d( "photo[${photo.id}] => from Database")
		return photo
	}

	private suspend fun getPhotoByIdFromServer(id: String): Photo {
		val info = api.webservice.getPhotoData(id)
		val photo = Photo(id, info.sizes.largeSquareSource, info.sizes.largeSource)
		photoDatabase.insert(photo)
		Timber.tag(TAG_PHOTO_REPO).d("photo[${photo.id}] => from Server")
		return photo
	}

}
