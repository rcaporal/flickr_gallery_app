package com.mindera.flickergallery.util.extesions

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Point
import android.graphics.Rect
import android.graphics.RectF
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.mindera.flickergallery.R
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception

fun View?.visible(visible: Boolean?){
	this?.visibility = if (visible == true) View.VISIBLE else View.GONE
}

fun ImageView?.load(url: String?, showPlaceHolder: Boolean = true, onComplete: (success: Boolean)->Unit = {}){
	if (!url.isNullOrEmpty()){
		val callback = object : Callback{
			override fun onSuccess() {
				onComplete(true)
			}

			override fun onError(e: Exception?) {
				onComplete(false)
			}

		}

		Picasso.get().load(url).apply {
			if (showPlaceHolder){
				placeholder(R.color.grey)
			}
		}.into(this, callback)
	}else{
		onComplete(false)
	}
}

fun ImageView?.load(@DrawableRes resource:Int?){
	resource?.let {
		this?.setImageResource(resource)
	}
}

fun View.expandImageFromThumb(thumbView: View, expandedImageView: View) {
	val shortAnimationDuration = this.context.resources.getInteger(android.R.integer.config_shortAnimTime)
	var currentAnimator: Animator? = null
	currentAnimator?.cancel()

	// Calculate the starting and ending bounds for the zoomed-in image.
	// This step involves lots of math. Yay, math.
	val startBoundsInt = Rect()
	val finalBoundsInt = Rect()
	val globalOffset = Point()

	// The start bounds are the global visible rectangle of the thumbnail,
	// and the final bounds are the global visible rectangle of the container
	// view. Also set the container view's offset as the origin for the
	// bounds, since that's the origin for the positioning animation
	// properties (X, Y).
	thumbView.getGlobalVisibleRect(startBoundsInt)
	this.getGlobalVisibleRect(finalBoundsInt, globalOffset)
	startBoundsInt.offset(-globalOffset.x, -globalOffset.y)
	finalBoundsInt.offset(-globalOffset.x, -globalOffset.y)

	val startBounds = RectF(startBoundsInt)
	val finalBounds = RectF(finalBoundsInt)

	// Adjust the start bounds to be the same aspect ratio as the final
	// bounds using the "center crop" technique. This prevents undesirable
	// stretching during the animation. Also calculate the start scaling
	// factor (the end scaling factor is always 1.0).
	val startScale: Float
	if ((finalBounds.width() / finalBounds.height() > startBounds.width() / startBounds.height())) {
		// Extend start bounds horizontally
		startScale = startBounds.height() / finalBounds.height()
		val startWidth: Float = startScale * finalBounds.width()
		val deltaWidth: Float = (startWidth - startBounds.width()) / 2
		startBounds.left -= deltaWidth.toInt()
		startBounds.right += deltaWidth.toInt()
	} else {
		// Extend start bounds vertically
		startScale = startBounds.width() / finalBounds.width()
		val startHeight: Float = startScale * finalBounds.height()
		val deltaHeight: Float = (startHeight - startBounds.height()) / 2f
		startBounds.top -= deltaHeight.toInt()
		startBounds.bottom += deltaHeight.toInt()
	}

	// Hide the thumbnail and show the zoomed-in view. When the animation
	// begins, it will position the zoomed-in view in the place of the
	// thumbnail.
	thumbView.alpha = 0f
	expandedImageView.visibility = View.VISIBLE

	// Set the pivot point for SCALE_X and SCALE_Y transformations
	// to the top-left corner of the zoomed-in view (the default
	// is the center of the view).
	expandedImageView.pivotX = 0f
	expandedImageView.pivotY = 0f

	// Construct and run the parallel animation of the four translation and
	// scale properties (X, Y, SCALE_X, and SCALE_Y).
	currentAnimator = AnimatorSet().apply {
		play(
			ObjectAnimator.ofFloat(
			expandedImageView,
			View.X,
			startBounds.left,
			finalBounds.left)
		).apply {
			with(ObjectAnimator.ofFloat(expandedImageView, View.Y, startBounds.top, finalBounds.top))
			with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X, startScale, 1f))
			with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_Y, startScale, 1f))
		}
		duration = shortAnimationDuration.toLong()
		interpolator = DecelerateInterpolator()
		addListener(object : AnimatorListenerAdapter() {

			override fun onAnimationEnd(animation: Animator) {
				currentAnimator = null
			}

			override fun onAnimationCancel(animation: Animator) {
				currentAnimator = null
			}
		})
		start()
	}

	// Upon clicking the zoomed-in image, it should zoom back down
	// to the original bounds and show the thumbnail instead of
	// the expanded image.
	expandedImageView.setOnClickListener {
		currentAnimator?.cancel()

		// Animate the four positioning/sizing properties in parallel,
		// back to their original values.
		currentAnimator = AnimatorSet().apply {
			play(ObjectAnimator.ofFloat(expandedImageView, View.X, startBounds.left)).apply {
				with(ObjectAnimator.ofFloat(expandedImageView, View.Y, startBounds.top))
				with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X, startScale))
				with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_Y, startScale))
			}
			duration = shortAnimationDuration.toLong()
			interpolator = DecelerateInterpolator()
			addListener(object : AnimatorListenerAdapter() {

				override fun onAnimationEnd(animation: Animator) {
					thumbView.alpha = 1f
					expandedImageView.visibility = View.GONE
					currentAnimator = null
				}

				override fun onAnimationCancel(animation: Animator) {
					thumbView.alpha = 1f
					expandedImageView.visibility = View.GONE
					currentAnimator = null
				}
			})
			start()
		}
	}
}
