package com.mindera.flickergallery.data.model.response

import com.mindera.flickergallery.data.model.PhotoSizes

data class PhotoDataResponse(
        val sizes: PhotoSizes,
        val stat: String
)
