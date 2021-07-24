package com.example.starwars.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.starwars.domain.usecase.GetPlanetUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PeopleViewModel @Inject constructor (
    private val getPlanetUseCase: GetPlanetUseCase,
    private val planetViewModel: PlanetViewModel,
    private val filmViewModel: FilmViewModel
        ): ViewModel() {


}