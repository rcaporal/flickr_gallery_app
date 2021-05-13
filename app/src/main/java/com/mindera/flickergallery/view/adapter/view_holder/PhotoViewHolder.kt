package com.mindera.flickergallery.view.adapter.view_holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.mindera.flickergallery.R
import com.mindera.flickergallery.data.model.Photo
import com.mindera.flickergallery.util.extesions.load
import com.mindera.flickergallery.util.extesions.visible
import com.mindera.flickergallery.view.adapter.PhotoListAdapter
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_photo_list.*

class PhotoViewHolder(itemView: View, private val callback: PhotoListAdapter.OnClickListener): RecyclerView.ViewHolder(itemView), LayoutContainer {
	override val containerView: View = itemView

	fun bindTo(item: Photo?) {
		photoListImage.setOnClickListener(null)
		if (item != null){
			photoListImage.load(item.largeSquareSource) { success ->
				loadingListImage.visible(false)
				if (success) {
					photoListImage.setOnClickListener {
						callback.onImageClick(photoListImage, item)
					}
				} else {
					photoListImage.load(R.drawable.ic_image_error)
				}
			}
		}else {
			loadingListImage.visible(false)
			photoListImage.load(R.drawable.ic_image_error)
		}
	}
}
