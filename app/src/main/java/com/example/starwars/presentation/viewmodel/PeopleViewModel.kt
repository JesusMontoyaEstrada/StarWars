package com.example.starwars.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.starwars.data.model.Film
import com.example.starwars.data.model.People
import com.example.starwars.data.model.Planet
import com.example.starwars.domain.usecase.GetFilmUseCase
import com.example.starwars.domain.usecase.GetPeopleUseCase
import com.example.starwars.domain.usecase.GetPlanetUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PeopleViewModel @Inject constructor (
    private val getPeopleUseCase: GetPeopleUseCase,
    private val getPlanetUseCase: GetPlanetUseCase,
    private val getFilmUseCase: GetFilmUseCase
        ): ViewModel() {

    private var currentResultStream : Flow<PagingData<People>>? = null

    fun getPeople(planet: Long? = null, film : Long? = null) : Flow<PagingData<People>> {
        var resultStream : Flow<PagingData<People>> = getPeopleUseCase.execute(planet, film).cachedIn(viewModelScope)
        currentResultStream = resultStream
        return resultStream
    }

    var planet : MutableLiveData<Planet> = MutableLiveData()

    fun getPlanet(id : Long) = viewModelScope.launch {
        planet.value = getPlanetUseCase.execute(id)
    }

    var film : MutableLiveData<Film> = MutableLiveData()

    fun getFilm(id : Long) = viewModelScope.launch {
        film.value = getFilmUseCase.execute(id)
    }

}