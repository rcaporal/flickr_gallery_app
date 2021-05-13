package com.mindera.flickergallery.data.model

data class PhotoSizes(
    val size: List<ImageSize>
){
	val largeSquareSource: String? get() {
		return size.find { it.label == "Large Square" }?.source
	}

	val largeSource: String? get() {
		return size.find { it.label == "Large" }?.source
	}
}
