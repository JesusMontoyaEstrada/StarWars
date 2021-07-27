package com.example.starwars.presentation.di

import com.example.starwars.domain.repository.FilmRepository
import com.example.starwars.domain.repository.PeopleRepository
import com.example.starwars.domain.repository.PlanetRepository
import com.example.starwars.domain.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

    @Singleton
    @Provides
    fun providerGetFilmsUseCase(filmRepository: FilmRepository) : GetFilmsUseCase {
        return GetFilmsUseCase(filmRepository)
    }

    @Singleton
    @Provides
    fun providerGetPeopleUseCase(peopleRepository: PeopleRepository) : GetPeopleUseCase {
        return GetPeopleUseCase(peopleRepository)
    }

    @Singleton
    @Provides
    fun providerGetPlanetsUseCase(planetRepository : PlanetRepository) : GetPlanetsUseCase {
        return GetPlanetsUseCase(planetRepository)
    }

    @Singleton
    @Provides
    fun providerGetFilmsByPageUseCase(filmRepository : FilmRepository) : GetFilmsByPageUseCase {
        return GetFilmsByPageUseCase(filmRepository)
    }

    @Singleton
    @Provides
    fun providerGetPeopleByPageUseCase(peopleRepository: PeopleRepository): GetPeopleByPageUseCase {
        return GetPeopleByPageUseCase(peopleRepository)
    }

    @Singleton
    @Provides
    fun providerGetPlanetsByPageUseCase(planetRepository: PlanetRepository): GetPlanetsByPageUseCase {
        return GetPlanetsByPageUseCase(planetRepository)
    }
}