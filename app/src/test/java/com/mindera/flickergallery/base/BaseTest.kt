package com.mindera.flickergallery.base
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.koin.core.context.GlobalContext
import org.koin.test.KoinTest


abstract class BaseTest: KoinTest {

	protected lateinit var mockServer: MockWebServer

	@Before
	open fun setUp(){
		configureMockServer()
	}

	@After
	open fun tearDown(){
		stopMockServer()
		GlobalContext.stop()
	}

	abstract fun isMockServerEnabled(): Boolean

	private fun configureMockServer(){
		if (isMockServerEnabled()){
			mockServer = MockWebServer()
			mockServer.start()
		}
	}

	private fun stopMockServer() {
		if (isMockServerEnabled()){
			mockServer.shutdown()
		}
	}

	fun getMockUrl() = mockServer.url("/").toString()

}
