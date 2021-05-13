package com.mindera.flickergallery.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mindera.flickergallery.R
import com.mindera.flickergallery.data.model.Photo
import com.mindera.flickergallery.util.Status
import com.mindera.flickergallery.view.adapter.view_holder.PhotoNetworkStateViewHolder
import com.mindera.flickergallery.view.adapter.view_holder.PhotoViewHolder

class PhotoListAdapter(private val callback: OnClickListener) :
		PagedListAdapter<Photo, RecyclerView.ViewHolder>(diffCallback) {

	private var requestState: Status? = null
	interface OnClickListener {
		fun onClickRetry()
		fun onImageClick(photoListImage: ImageView, photo: Photo?)
		fun whenListIsUpdated(size: Int, networkState: Status?)
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
		val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
		return when (viewType) {
			R.layout.item_photo_list -> PhotoViewHolder(view, callback)
			R.layout.item_photo_request_state -> PhotoNetworkStateViewHolder(view)
			else -> throw IllegalArgumentException("Unknown view type $viewType")
		}
	}

	override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
		when (getItemViewType(position)) {
			R.layout.item_photo_list -> (holder as PhotoViewHolder).bindTo(getItem(position))
			R.layout.item_photo_request_state -> (holder as PhotoNetworkStateViewHolder).bindTo(requestState, callback)
		}
	}

	override fun getItemViewType(position: Int): Int {
		return if (hasExtraPhoto() && position == itemCount - 1) {
			R.layout.item_photo_request_state
		} else {
			R.layout.item_photo_list
		}
	}

	private fun hasExtraPhoto() = requestState != null && requestState != Status.SUCCESS

	fun updateNetworkState(newNetworkState: Status?) {
		val previousState = this.requestState
		val hadExtraPhoto = hasExtraPhoto()
		this.requestState = newNetworkState
		val hasExtraPhoto = hasExtraPhoto()
		if (hadExtraPhoto != hasExtraPhoto) {
			if (hadExtraPhoto) {
				notifyItemRemoved(super.getItemCount())
			} else {
				notifyItemInserted(super.getItemCount())
			}
		} else if (hasExtraPhoto && previousState != newNetworkState) {
			notifyItemChanged(itemCount - 1)
		}
	}

	override fun getItemCount(): Int {
		this.callback.whenListIsUpdated(super.getItemCount(), this.requestState)
		return super.getItemCount()
	}

	companion object {
		private val diffCallback = object : DiffUtil.ItemCallback<Photo>() {
			override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean = oldItem == newItem
			override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean = oldItem == newItem
		}
	}

}
