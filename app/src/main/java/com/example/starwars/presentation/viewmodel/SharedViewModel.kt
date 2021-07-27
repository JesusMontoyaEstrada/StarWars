package com.example.starwars.presentation.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.starwars.data.model.Film
import com.example.starwars.data.model.Planet

class SharedViewModel : ViewModel(){
    val selectedFilm = MutableLiveData<Film?>()
    val selectedPlanet = MutableLiveData<Planet?>()

    val filters = MediatorLiveData<Pair<Planet?, Film?>>().apply {
        addSource(selectedPlanet) {
            this.value = Pair(it, this.value?.second)
        }
        addSource(selectedFilm) {
            this.value = Pair(this.value?.first, it)
        }
    }
}