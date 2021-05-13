package com.mindera.flickergallery.data.pagination

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.mindera.flickergallery.data.model.Photo
import com.mindera.flickergallery.data.repository.PhotoRepository
import kotlinx.coroutines.CoroutineScope

class PhotoDataSourceFactory(private val repository: PhotoRepository,
							 private val scope: CoroutineScope): DataSource.Factory<Int, Photo>() {

    val source = MutableLiveData<PhotoDataSource>()

    override fun create(): DataSource<Int, Photo> {
        val source = PhotoDataSource(repository, scope)
        this.source.postValue(source)
        return source
    }

    fun getSource() = source.value
}
