package com.example.starwars.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.starwars.data.model.Planet
import com.example.starwars.domain.usecase.GetPlanetUseCase
import com.example.starwars.domain.usecase.GetPlanetsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PlanetViewModel @Inject constructor(
    private val getPlanetUseCase: GetPlanetUseCase,
    private val getPlanetsUseCase: GetPlanetsUseCase
): ViewModel(){

    private var currentResultStream : Flow<PagingData<Planet>>? = null

    fun getPlanets() : Flow<PagingData<Planet>> {
        val resultStream : Flow<PagingData<Planet>> = getPlanetsUseCase.execute()
            .cachedIn(viewModelScope)
        currentResultStream = resultStream
        return resultStream
    }

    var planet : MutableLiveData<Planet> = MutableLiveData()

    fun getPlanet(id : Long) = viewModelScope.launch {
        planet.value = getPlanetUseCase.execute(id)
    }

}