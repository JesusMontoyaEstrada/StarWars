package com.example.starwars.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.starwars.domain.usecase.GetPlanetUseCase
import com.example.starwars.domain.usecase.GetPlanetsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class PlanetViewModel @Inject constructor(
    private val getPlanetUseCase: GetPlanetUseCase,
    private val getPlanetsUseCase: GetPlanetsUseCase
): ViewModel(){

}