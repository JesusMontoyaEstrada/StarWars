package com.example.starwars.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.starwars.data.model.Planet
import com.example.starwars.domain.usecase.GetPlanetsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


@HiltViewModel
class PlanetViewModel @Inject constructor(
    private val getPlanetsUseCase: GetPlanetsUseCase
): ViewModel(){

    private var currentResultStream : Flow<PagingData<Planet>>? = null

    fun getPlanets() : Flow<PagingData<Planet>> {
        val resultStream : Flow<PagingData<Planet>> = getPlanetsUseCase.execute()
            .cachedIn(viewModelScope)
        currentResultStream = resultStream
        return resultStream
    }

}