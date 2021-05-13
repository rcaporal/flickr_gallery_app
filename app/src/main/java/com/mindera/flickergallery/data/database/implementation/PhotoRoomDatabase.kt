package com.mindera.flickergallery.data.database.implementation

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mindera.flickergallery.data.database.PhotoDAO
import com.mindera.flickergallery.data.model.Photo

@Database(entities = [Photo::class], version = 1, exportSchema = false)
abstract class PhotoRoomDatabase : RoomDatabase() {
	abstract fun photoDao(): PhotoDAO
}
