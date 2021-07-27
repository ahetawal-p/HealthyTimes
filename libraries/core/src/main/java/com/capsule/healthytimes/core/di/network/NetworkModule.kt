package com.capsule.healthytimes.core.di.network

import com.capsule.healthytimes.core.BuildConfig
import com.capsule.healthytimes.core.domain.respository.ArticleService
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import okhttp3.logging.HttpLoggingInterceptor.Level.NONE
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton


@Module
class NetworkModule {

    companion object {
        const val BASE_URL = "https://api.nytimes.com/svc/search/v2/"
        const val API_IMAGE_BASE_URL = "https://www.nytimes.com/"
    }

    @Provides
    @Singleton
    fun provideArticlesApi(retrofit: Retrofit): ArticleService {
        return ArticleService(retrofit)
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create().withNullSerialization())
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttp(): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder()
        okHttpClientBuilder.addInterceptor(ApiKeyInterceptor())
        val logLevel = if (BuildConfig.DEBUG) BODY else NONE
        okHttpClientBuilder.addInterceptor(HttpLoggingInterceptor().setLevel(logLevel))

        return okHttpClientBuilder.build()
    }


}

class ApiKeyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var original = chain.request()
        val newUrl = original.url.newBuilder()
            .addQueryParameter("api-key", BuildConfig.API_KEY).build()
        original = original.newBuilder().url(newUrl).build()
        return chain.proceed(original)
    }
}