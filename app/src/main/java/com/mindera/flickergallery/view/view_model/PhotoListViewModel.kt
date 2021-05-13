package com.mindera.flickergallery.view.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.mindera.flickergallery.data.pagination.PhotoDataSourceFactory
import com.mindera.flickergallery.data.repository.PhotoRepository
import com.mindera.flickergallery.util.Status

class PhotoListViewModel(repository: PhotoRepository) : BaseViewModel() {

	private val INITIAL_PAGE_SIZE = 15
	private val userDataSource = PhotoDataSourceFactory(repository = repository, scope = ioScope)

	val photos = LivePagedListBuilder(userDataSource, pagedListConfig()).build()
	val networkState: LiveData<Status> = Transformations.switchMap( userDataSource.source ) { it.getNetworkState() }

	fun fetchPhotos() {
		userDataSource.getSource()?.refresh()
	}

	fun refreshFailedRequest() =
			userDataSource.getSource()?.retryFailedRequest()

	fun refreshAllList() =
			userDataSource.getSource()?.refresh()

	private fun pagedListConfig() = PagedList.Config.Builder()
			.setInitialLoadSizeHint(INITIAL_PAGE_SIZE)
			.setEnablePlaceholders(false)
			.setPageSize(INITIAL_PAGE_SIZE * 2)
			.build()

}
