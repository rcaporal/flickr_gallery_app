package com.mindera.flickergallery.util

import android.content.Context
import com.mindera.flickergallery.util.extesions.isOnline

class NetworkHelperImpl(private val context: Context): NetworkHelper {
	override fun isOnline(): Boolean {
		return context.isOnline()
	}
}

interface NetworkHelper {
	fun isOnline(): Boolean
}
