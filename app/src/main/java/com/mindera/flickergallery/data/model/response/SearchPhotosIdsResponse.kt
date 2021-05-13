package com.mindera.flickergallery.data.model.response

import com.mindera.flickergallery.data.model.PhotosPage

data class SearchPhotosIdsResponse(
        val photos: PhotosPage,
        val stat: String,
        val perpage: Int,
        val total: String,
        val pages: Int
)
