package com.example.starwars.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.starwars.domain.usecase.GetFilmUseCase
import com.example.starwars.domain.usecase.GetFilmsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class FilmViewModel @Inject constructor(
    private val getFilmUseCase: GetFilmUseCase,
    private val getFilmsUseCase: GetFilmsUseCase
): ViewModel(){
}