package com.mindera.flickergallery.di

import androidx.room.Room
import com.mindera.flickergallery.data.database.PhotosDatabase
import com.mindera.flickergallery.data.database.implementation.PhotoDatabaseImpl
import com.mindera.flickergallery.data.database.implementation.PhotoRoomDatabase
import org.koin.dsl.module

val databaseModule = module {
	single <PhotosDatabase> {
		PhotoDatabaseImpl(
			Room.databaseBuilder( get(), PhotoRoomDatabase::class.java, "photoDb").build().photoDao()
		)
	}
}
