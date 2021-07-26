package com.example.starwars.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface Repository<T : Any> {

    fun getResultStream() : Flow<PagingData<T>>
    suspend fun getItem(id: Long) : T
}