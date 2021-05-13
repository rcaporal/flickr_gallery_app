package com.mindera.flickergallery.view.adapter.view_holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.mindera.flickergallery.R
import com.mindera.flickergallery.util.Status
import com.mindera.flickergallery.util.extesions.visible
import com.mindera.flickergallery.view.adapter.PhotoListAdapter
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_photo_request_state.*

class PhotoNetworkStateViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), LayoutContainer {
	override val containerView: View = itemView

	fun bindTo(networkState: Status?, callback: PhotoListAdapter.OnClickListener){
		hideViews()
		setVisibleRightViews(networkState)
		retryButtonNetworkState.setOnClickListener {
			callback.onClickRetry()
		}
	}

	private fun hideViews() {
		retryButtonNetworkState.visible(false)
		textButtonRequestState.visible(false)
		loadingNetworkState.visible(false)
	}

	private fun setVisibleRightViews(requestState: Status?) {
		when (requestState) {
			Status.ERROR -> {
				retryButtonNetworkState.visible(true)
				textButtonRequestState.text = containerView.context.getString(R.string.oops_an_error_happened)
				textButtonRequestState.visible(true)
			}
			Status.NO_CONNECTION -> {
				retryButtonNetworkState.visible(true)
				textButtonRequestState.text = containerView.context.getString(R.string.connection_error)
				textButtonRequestState.visible(true)
			}
			Status.LOADING -> {
				textButtonRequestState.visible(false)
				retryButtonNetworkState.visible(false)
				loadingNetworkState.visible(true)
			}
		}
	}
}
