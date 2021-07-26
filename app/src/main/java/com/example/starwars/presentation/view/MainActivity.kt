package com.example.starwars.presentation.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.navigation.findNavController
import com.example.starwars.R
import com.example.starwars.data.model.Film
import com.example.starwars.data.model.Planet
import com.example.starwars.databinding.ActivityMainBinding
import com.example.starwars.presentation.adapter.FilmAdapter
import com.example.starwars.presentation.adapter.PeopleAdapter
import com.example.starwars.presentation.adapter.PlanetAdapter
import com.example.starwars.presentation.viewmodel.FilmViewModel
import com.example.starwars.presentation.viewmodel.PeopleViewModel
import com.example.starwars.presentation.viewmodel.PlanetViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    val filmViewModel : FilmViewModel by viewModels()
    var filmAdapter = FilmAdapter()
    private var selectedFilm : Film? = null

    val planetViewModel: PlanetViewModel by viewModels()
    var planetAdapter = PlanetAdapter()
    private var selectedPlanet : Planet? = null

    val peopleViewModel: PeopleViewModel by viewModels()
    var peopleAdapter = PeopleAdapter()

    lateinit var binding : ActivityMainBinding

    var filterView : Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.filters.setOnClickListener {

            filterView = !filterView

            if(filterView){
                binding.filters.text = "Cerrar"
                findNavController(R.id.fragment).navigate(
                    R.id.action_peopleFragment_to_filterFragment
                )
            } else {
                binding.filters.text = "Filtros"
                onBackPressed()
            }
        }

        chipListener()
    }

    fun setPlanetValue(planet : Planet?){
        selectedPlanet = planet
        planet?.let { it ->
            binding.planetChip.text = it.name
            binding.planetChip.visibility = View.VISIBLE
        }
    }

    fun getPlanetValue() : Planet? {
        return selectedPlanet
    }

    fun setFilmValue(film : Film?){
        selectedFilm = film
        film?.let {
            binding.filmChip.text = film.name
            binding.filmChip.visibility = View.VISIBLE
        }
    }

    fun getFilmValue() : Film?{
        return selectedFilm
    }

    private fun chipListener(){
        binding.filmChip.setOnCloseIconClickListener {
            setFilmValue(null)
            binding.filmChip.visibility = View.INVISIBLE
            binding.filmChip.text = ""
            updateFilters()
        }

        binding.planetChip.setOnCloseIconClickListener {
            setPlanetValue(null)
            binding.planetChip.visibility = View.INVISIBLE
            binding.planetChip.text = ""
            updateFilters()
        }
    }

    fun updateFilters(){
        val navHost = supportFragmentManager.findFragmentById(R.id.fragment)
        navHost?.let { navFragment ->
            navFragment.childFragmentManager.primaryNavigationFragment?.let { fragment->
                if(fragment is PeopleFragment){
                    fragment.filterListener(getPlanetValue(), getFilmValue())
                }
            }
        }
    }
}