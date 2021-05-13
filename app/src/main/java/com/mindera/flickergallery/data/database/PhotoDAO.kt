package com.mindera.flickergallery.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.mindera.flickergallery.data.model.Photo

@Dao
interface PhotoDAO {

	@Query("SELECT * FROM photo WHERE id = :id")
	suspend fun getPhoto(id: String): Photo?

	@Query("SELECT * FROM photo")
	suspend fun getAllPhotos(): List<Photo>

	@Insert
	suspend fun insert(photo: Photo)

	@Delete
	suspend fun delete(photo: Photo)

}
