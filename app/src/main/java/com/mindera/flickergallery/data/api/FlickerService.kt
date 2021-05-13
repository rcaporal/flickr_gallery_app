package com.mindera.flickergallery.data.api

import com.mindera.flickergallery.BuildConfig
import com.mindera.flickergallery.data.model.response.PhotoDataResponse
import com.mindera.flickergallery.data.model.response.SearchPhotosIdsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface FlickerService {

	@GET("services/rest/")
	suspend fun getPhotosIds(
		@Query("page") page: Int,
		@Query("per_page") perPage: Int,
		@Query("api_key") apiKey: String = BuildConfig.API_KEY,
		@Query("method") method: String = "flickr.photos.search",
		@Query("tags") tags: String = "kitten",
		@Query("format") format: String = "json",
		@Query("nojsoncallback") noJsonCallback: Int = 1
	) : SearchPhotosIdsResponse

	@GET("services/rest/")
	suspend fun getPhotoData(
			@Query("photo_id") photoId: String,
			@Query("api_key") api_key: String = BuildConfig.API_KEY,
			@Query("method") method: String = "flickr.photos.getSizes",
			@Query("format") format: String = "json",
			@Query("nojsoncallback") noJsonCallback: Int = 1
	) : PhotoDataResponse

}
