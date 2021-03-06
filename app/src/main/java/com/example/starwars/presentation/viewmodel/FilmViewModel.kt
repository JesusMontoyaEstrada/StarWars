package com.example.starwars.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.starwars.data.model.Film
import com.example.starwars.domain.usecase.GetFilmsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


@HiltViewModel
class FilmViewModel @Inject constructor(
    private val getFilmsUseCase: GetFilmsUseCase
): ViewModel(){

    private var currentResultStream : Flow<PagingData<Film>> ? = null

    fun getFilms() : Flow<PagingData<Film>> {
        val resultStream : Flow<PagingData<Film>> = getFilmsUseCase.execute()
            .cachedIn(viewModelScope)
        currentResultStream = resultStream
        return resultStream
    }

}