package com.example.starwars.presentation.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import com.example.starwars.BuildConfig
import com.example.starwars.data.api.FilmAPIService
import com.example.starwars.data.api.PeopleAPIService
import com.example.starwars.data.api.PlanetAPIService

@Module
@InstallIn(SingletonComponent::class)
class NetModule {

    @Singleton
    @Provides
    fun providerRetrofit() : Retrofit {
        val interceptor = HttpLoggingInterceptor().apply {
            this.level  = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder().apply {
            this.addInterceptor(interceptor)
        }.build()

        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.URL)
            .client(client)
            .build()
    }


    @Singleton
    @Provides
    fun providerFilmAPIService(retrofit : Retrofit) : FilmAPIService {
        return retrofit.create(FilmAPIService::class.java)
    }

    @Singleton
    @Provides
    fun providerPeopleAPIService(retrofit : Retrofit) : PeopleAPIService {
        return retrofit.create(PeopleAPIService::class.java)
    }

    @Singleton
    @Provides
    fun providerPlanetAPIService(retrofit : Retrofit) : PlanetAPIService {
        return retrofit.create(PlanetAPIService::class.java)
    }

}