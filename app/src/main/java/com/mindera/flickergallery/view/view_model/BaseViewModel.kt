package com.mindera.flickergallery.view.view_model

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel

open class BaseViewModel: ViewModel() {
	/**
	 * This is a scope for all coroutines launched by [BaseViewModel]
	 * that will be dispatched in Main Thread
	 */
	private val uiScope = CoroutineScope(Dispatchers.Main)

	/**
	 * This is a scope for all coroutines launched by [BaseViewModel]
	 * that will be dispatched in a Pool of Thread
	 */
	protected val ioScope = CoroutineScope(Dispatchers.IO)

	/**
	 * Cancel all coroutines when the ViewModel is cleared
	 */
	override fun onCleared() {
		super.onCleared()
		uiScope.coroutineContext.cancel()
		ioScope.coroutineContext.cancel()
	}
}
