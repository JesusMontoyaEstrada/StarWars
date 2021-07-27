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
import com.example.starwars.presentation.viewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding

    var filterView : Boolean = false

    val sharedViewModel: SharedViewModel by viewModels()

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

        sharedViewModel.selectedPlanet.observe(this, { planet ->
            planet?.let {
                binding.planetChip.text = it.name
                binding.planetChip.visibility = View.VISIBLE
            }
        })

        sharedViewModel.selectedFilm.observe(this, { film ->
            film?.let {
                binding.filmChip.text = it.name
                binding.filmChip.visibility = View.VISIBLE
            }
        })

        chipListener()
    }


    private fun chipListener(){
        binding.filmChip.setOnCloseIconClickListener {
            sharedViewModel.selectedFilm.postValue(null)
            binding.filmChip.visibility = View.INVISIBLE
            binding.filmChip.text = ""
        }

        binding.planetChip.setOnCloseIconClickListener {
            sharedViewModel.selectedPlanet.postValue(null)
            binding.planetChip.visibility = View.INVISIBLE
            binding.planetChip.text = ""
        }
    }

}