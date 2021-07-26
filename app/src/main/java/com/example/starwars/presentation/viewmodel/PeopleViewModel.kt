package com.example.starwars.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.starwars.data.model.People
import com.example.starwars.domain.usecase.GetPeopleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


@HiltViewModel
class PeopleViewModel @Inject constructor (
    private val getPeopleUseCase: GetPeopleUseCase
        ): ViewModel() {

    private var currentResultStream : Flow<PagingData<People>>? = null

    fun getPeople(planet: Long? = null, film : Long? = null) : Flow<PagingData<People>> {
        var resultStream : Flow<PagingData<People>> = getPeopleUseCase.execute(planet, film).cachedIn(viewModelScope)
//        currentResultStream = resultStream
        return resultStream
    }


}