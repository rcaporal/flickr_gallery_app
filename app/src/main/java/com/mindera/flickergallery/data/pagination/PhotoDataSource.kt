package com.mindera.flickergallery.data.pagination

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.mindera.flickergallery.data.model.Photo
import com.mindera.flickergallery.data.repository.PhotoRepository
import com.mindera.flickergallery.util.Status
import com.mindera.flickergallery.util.ThereIsNoPhotosOnDatabase
import kotlinx.coroutines.*
import timber.log.Timber

const val TAG_PHOTO_DATA_SOURCE = "TAG_PHOTO_DATA_SOURCE"

class PhotoDataSource(private val repository: PhotoRepository,
					  private val scope: CoroutineScope): PageKeyedDataSource<Int, Photo>() {

    private var supervisorJob = SupervisorJob()
    private val networkState = MutableLiveData<Status>()
    private var retryRequest: (() -> Any)? = null // Keep reference of the last request (to be able to retry it if necessary)

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Photo>) {
        retryRequest = { loadInitial(params, callback) }
        executeRequest(1, params.requestedLoadSize) {
            callback.onResult(it, null, 2)
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Photo>) {
        val page = params.key
        retryRequest = { loadAfter(params, callback) }
        executeRequest(page, params.requestedLoadSize) {
            callback.onResult(it, page + 1)
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Photo>) { }

    private fun executeRequest(page: Int, perPage: Int, callback:(List<Photo>) -> Unit) {
        networkState.postValue(Status.LOADING)
        scope.launch(getJobErrorHandler() + supervisorJob) {
            delay(200) // To handle Photo typing case
            val photos = repository.getPhotos(page, perPage)
            retryRequest = null
            networkState.postValue(Status.SUCCESS)
            callback(photos)
        }
    }

    private fun getJobErrorHandler() = CoroutineExceptionHandler { _, e ->
        Timber.tag(TAG_PHOTO_DATA_SOURCE).e(PhotoDataSource::class.java.simpleName, "An error happened: $e")
		when(e){
			is ThereIsNoPhotosOnDatabase -> networkState.postValue(Status.NO_CONNECTION)
			else -> networkState.postValue(Status.ERROR)

		}
    }

    override fun invalidate() {
        super.invalidate()
        supervisorJob.cancelChildren()   // Cancel possible running job to only keep last request
    }

    fun getNetworkState(): LiveData<Status> = networkState

    fun refresh() = this.invalidate()

    fun retryFailedRequest() {
        val prevRequest = retryRequest
        retryRequest = null
		prevRequest?.invoke()
    }
}
