package com.divpundir.template.android.core.network

import com.divpundir.template.android.core.preferences.AccountPreference
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.slack.eithernet.ApiResultCallAdapterFactory
import com.slack.eithernet.ApiResultConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder().build()

    @Provides
    @Singleton
    fun provideRetrofit(
        json: Json,
        okHttpClient: OkHttpClient,
        accPrefManager: AccountPreference.Manager
    ): Retrofit = Retrofit.Builder()
        .baseUrl(NetworkConstants.API_BASE_URL)
        .client(
            okHttpClient
                .newBuilder()
                .addInterceptor(HeaderInterceptor("Content-Type", "application/json"))
                .addInterceptor(AutoAuthInterceptor { accPrefManager.authToken })
                .build()
        )
        .addConverterFactory(ApiResultConverterFactory)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .addCallAdapterFactory(ApiResultCallAdapterFactory)
        .build()
}
