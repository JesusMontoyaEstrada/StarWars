package com.example.starwars.presentation.di

import com.example.starwars.data.api.FilmAPIService
import com.example.starwars.data.api.PeopleAPIService
import com.example.starwars.data.api.PlanetAPIService
import com.example.starwars.data.model.Film
import com.example.starwars.data.model.Planet
import com.example.starwars.data.repository.film.FilmRepositoryImpl
import com.example.starwars.data.repository.people.PeopleRepositoryImpl
import com.example.starwars.data.repository.planet.PlanetRepositoryImpl
import com.example.starwars.domain.repository.PeopleRepository
import com.example.starwars.domain.repository.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn (SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun providerPeopleRepository(peopleAPIService: PeopleAPIService) : PeopleRepository {
        return PeopleRepositoryImpl(peopleAPIService)
    }

    @Singleton
    @Provides
    fun providerFilmRepository(filmAPIService: FilmAPIService) : Repository<Film> {
        return FilmRepositoryImpl(filmAPIService)
    }

    @Singleton
    @Provides
    fun providerPlanetRepository(planetAPIService: PlanetAPIService) : Repository<Planet> {
        return PlanetRepositoryImpl(planetAPIService)
    }
}