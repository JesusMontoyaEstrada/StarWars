package com.example.starwars.presentation.di

import com.example.starwars.presentation.viewmodel.PeopleViewModel
import com.example.starwars.presentation.viewmodel.PlanetViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class ViewModelModule {

//    @Singleton
//    @Provides
//    fun providerPlanetViewModel(planetViewModel: PlanetViewModel) : PeopleViewModel {
//        return PeopleViewModel(planetViewModel)
//    }

}