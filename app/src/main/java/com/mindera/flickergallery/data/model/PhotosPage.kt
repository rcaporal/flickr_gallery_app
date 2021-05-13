package com.mindera.flickergallery.data.model

data class PhotosPage(
        val page: Int,
        val pages: Int,
        val perpage: Int,
        val photo: List<PhotoInfo>,
        val total: String
)
