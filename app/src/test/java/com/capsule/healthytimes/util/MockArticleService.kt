package com.capsule.healthytimes.util

import com.capsule.healthytimes.core.domain.respository.ArticleService
import io.mockk.spyk
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

fun getSpyedMockApiService(url: HttpUrl): ArticleService {
    val client = OkHttpClient.Builder()
        .connectTimeout(1, TimeUnit.SECONDS)
        .readTimeout(1, TimeUnit.SECONDS)
        .writeTimeout(1, TimeUnit.SECONDS)
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl(url)
        .client(client)
        .addConverterFactory(MoshiConverterFactory.create().withNullSerialization())
        .build()

    return spyk(ArticleService(retrofit))
}

@Throws(IOException::class)
fun readFileToString(
    contextClass: Class<*>,
    streamIdentifier: String
): String {
    return contextClass.getResource(streamIdentifier)?.readText() ?: throw IOException()
}

@Throws(IOException::class)
fun mockResponse(fileName: String, contextClass: Class<*>): MockResponse {
    return MockResponse().setBody(readFileToString(contextClass, "/$fileName"))
}