package com.mindera.flickergallery.view.activity

import android.content.res.Configuration
import android.os.Bundle
import android.os.Parcelable
import android.widget.ImageView
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.mindera.flickergallery.R
import com.mindera.flickergallery.data.model.Photo
import com.mindera.flickergallery.util.Status
import com.mindera.flickergallery.util.extesions.expandImageFromThumb
import com.mindera.flickergallery.util.extesions.load
import com.mindera.flickergallery.util.extesions.visible
import com.mindera.flickergallery.view.adapter.PhotoListAdapter
import com.mindera.flickergallery.view.view_model.PhotoListViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_loading.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), PhotoListAdapter.OnClickListener {

	private val photoListViewModel: PhotoListViewModel by viewModel()
	private lateinit var adapter: PhotoListAdapter
	private lateinit var gridLayoutManager: GridLayoutManager

	override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
		setupRecycler()
		setupObservers()
		getPhotoList()
    }

	private fun setupObservers() {
		photoListViewModel.photos.observe(this, Observer { adapter.submitList(it) })
		photoListViewModel.networkState.observe(this, Observer { adapter.updateNetworkState(it) })
	}

	private fun setupRecycler() {
		adapter = PhotoListAdapter(this)
		gridLayoutManager = GridLayoutManager(this, 2)
		mainRecycler.layoutManager = gridLayoutManager
		mainRecycler.adapter = adapter
	}

	override fun onConfigurationChanged(newConfig: Configuration) {
		super.onConfigurationChanged(newConfig)
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
			gridLayoutManager.spanCount = 3
		}else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
			gridLayoutManager.spanCount = 2
		}
	}

	private fun getPhotoList() {
		loadingLayout.visible(true)
		photoListViewModel.fetchPhotos()
	}

	override fun onClickRetry() {
		photoListViewModel.refreshFailedRequest()
	}

	override fun onImageClick(photoListImage: ImageView, photo: Photo?) {
		expandedImage.load(photo?.largeSource, false) { successLoaded ->
			if (!successLoaded){
				expandedImage.load(photo?.largeSquareSource){ retrySuccessLoaded ->
					if (!retrySuccessLoaded){
						expandedImage.load(R.drawable.ic_image_error)
					}
				}
			}
		}
		rootLayout.expandImageFromThumb(photoListImage, expandedImage)
	}

	override fun whenListIsUpdated(size: Int, networkState: Status?) {
		loadingLayout.visible(false)
		updateUIWhenLoading(size, networkState)
		updateUIWhenEmptyList(size, networkState)
	}

	private fun updateUIWhenEmptyList(size: Int, networkState: Status?) {
		if (size == 0) {
			when (networkState) {
				Status.SUCCESS -> {
					showErrorDialog(R.string.empty_list_message) { photoListViewModel.refreshAllList() }
				}
				Status.NO_CONNECTION -> {
					loadingLayout.visible(true)
					showErrorDialog(R.string.connection_error) { photoListViewModel.refreshAllList() }
				}
				Status.ERROR -> {
					loadingLayout.visible(true)
					showErrorDialog { photoListViewModel.refreshAllList() }
				}
			}
		}
	}

	private fun updateUIWhenLoading(size: Int, networkState: Status?) {
		loadingLayout.visible(size == 0 && networkState == Status.LOADING)
	}

	private fun showErrorDialog(@StringRes errorStringSource: Int? = null, onRetry: () -> Unit) {
		MaterialDialog(this).lifecycleOwner(this).show {
			title(R.string.error_title)
			message(errorStringSource ?: R.string.error_default)
			cancelable(false)
			positiveButton(R.string.retry) { onRetry() }
			negativeButton(R.string.cancel) { finish() }
		}
	}
}
