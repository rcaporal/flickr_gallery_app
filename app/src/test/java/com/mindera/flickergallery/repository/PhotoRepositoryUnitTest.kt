package com.mindera.flickergallery.repository

import com.mindera.flickergallery.base.BaseUT
import com.mindera.flickergallery.data.repository.PhotoRepository
import com.mindera.flickergallery.di.configureAppComponent
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.test.inject
import retrofit2.HttpException
import java.net.HttpURLConnection

class PhotoRepositoryUnitTest : BaseUT() {

	override fun setUp() {
		super.setUp()
		startKoin {
			modules(configureAppComponent(getMockUrl()))
		}
	}

	private val userRepository by inject<PhotoRepository>()

	@Test
	fun `get a photo page with 10 items and succeed`() {
		mockHttpResponse("photos_search_10.json", HttpURLConnection.HTTP_OK)
		mockHttpResponse("photos_get_sizes.json", HttpURLConnection.HTTP_OK)
		mockHttpResponse("photos_get_sizes.json", HttpURLConnection.HTTP_OK)
		mockHttpResponse("photos_get_sizes.json", HttpURLConnection.HTTP_OK)
		mockHttpResponse("photos_get_sizes.json", HttpURLConnection.HTTP_OK)
		mockHttpResponse("photos_get_sizes.json", HttpURLConnection.HTTP_OK)
		mockHttpResponse("photos_get_sizes.json", HttpURLConnection.HTTP_OK)
		mockHttpResponse("photos_get_sizes.json", HttpURLConnection.HTTP_OK)
		mockHttpResponse("photos_get_sizes.json", HttpURLConnection.HTTP_OK)
		mockHttpResponse("photos_get_sizes.json", HttpURLConnection.HTTP_OK)
		mockHttpResponse("photos_get_sizes.json", HttpURLConnection.HTTP_OK)

		runBlocking {
			val photos = userRepository.getPhotos(1, 10)
			assertEquals(10, photos.size)
			photos.forEach {
				assertEquals(false, it.id.isEmpty())
				assertEquals(false, it.largeSource.isNullOrEmpty())
				assertEquals(false, it.largeSquareSource.isNullOrEmpty())
			}
		}
	}

	@Test(expected = HttpException::class)
	fun `search photos by id and fail`() {
	mockHttpResponse("photos_search_1.json", HttpURLConnection.HTTP_OK)
	mockHttpResponse("photos_get_sizes_not_find.json", HttpURLConnection.HTTP_NOT_FOUND)
		runBlocking {
			userRepository.getPhotos(1,1)
		}
	}

	@Test
	fun `search photos by id and succeed`() {
	mockHttpResponse("photos_search_1.json", HttpURLConnection.HTTP_OK)
	mockHttpResponse("photos_get_sizes.json", HttpURLConnection.HTTP_OK)
		runBlocking {
			val photo = userRepository.getPhotos(1,1).first()
			assertEquals("51016983496", photo.id)
			assertEquals("https://live.staticflickr.com/5800/31456463045_5a0af4ddc8_q.jpg", photo.largeSquareSource)
			assertEquals("https://live.staticflickr.com/5800/31456463045_5a0af4ddc8_b.jpg", photo.largeSource)
		}
	}


	override fun isMockServerEnabled(): Boolean = true
}
