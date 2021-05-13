package com.mindera.flickergallery.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Photo(
	@PrimaryKey(autoGenerate = false) val id: String,
	val largeSquareSource: String?,
	val largeSource: String?
)
