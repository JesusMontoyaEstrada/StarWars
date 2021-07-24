package com.example.starwars.presentation.di

import com.example.starwars.data.model.Film
import com.example.starwars.data.model.Planet
import com.example.starwars.domain.repository.PeopleRepository
import com.example.starwars.domain.repository.Repository
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
    fun providerGetFilmsUseCase(filmRepository: Repository<Film>) : GetFilmsUseCase {
        return GetFilmsUseCase(filmRepository)
    }

    @Singleton
    @Provides
    fun providerGetFilmUseCase(filmRepository: Repository<Film>) : GetFilmUseCase {
        return GetFilmUseCase(filmRepository)
    }

    @Singleton
    @Provides
    fun providerGetPeopleUseCase(peopleRepository: PeopleRepository) : GetPeopleUseCase {
        return GetPeopleUseCase(peopleRepository)
    }


    @Singleton
    @Provides
    fun providerGetPlanetsUseCase(planetRepository : Repository<Planet>) : GetPlanetsUseCase {
        return GetPlanetsUseCase(planetRepository)
    }

    @Singleton
    @Provides
    fun providerGetPlanetUseCase(planetRepository: Repository<Planet>) : GetPlanetUseCase {
        return GetPlanetUseCase(planetRepository)
    }

}