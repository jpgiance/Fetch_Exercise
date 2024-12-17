package com.autonomy_lab.fetchexercise.di

import android.content.Context
import com.autonomy_lab.fetchexercise.BuildConfig
import com.autonomy_lab.fetchexercise.network.FetchApi
import com.autonomy_lab.fetchexercise.network.InternetConnectionObserver
import com.autonomy_lab.fetchexercise.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @Provides
    @Singleton
    fun retrofit(): Retrofit {
        val httpClient = OkHttpClient.Builder().run {
            addInterceptor(HttpLoggingInterceptor().apply {
                if (BuildConfig.DEBUG){
                    level = HttpLoggingInterceptor.Level.BODY
                }
            })
            build()
        }

        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(httpClient)
            .build()
    }


    @Provides
    fun fetchApi(retrofit: Retrofit): FetchApi{
        return retrofit.create(FetchApi::class.java)
    }

    @Provides
    @Singleton
    fun internetConnectionObserver(@ApplicationContext context: Context): InternetConnectionObserver{
        return InternetConnectionObserver(context)
    }
}