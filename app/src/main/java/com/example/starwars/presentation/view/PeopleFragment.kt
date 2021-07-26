package com.example.starwars.presentation.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.starwars.R
import com.example.starwars.data.model.Film
import com.example.starwars.data.model.People
import com.example.starwars.data.model.Planet
import com.example.starwars.databinding.FragmentPeopleBinding
import com.example.starwars.presentation.adapter.LoadStateAdapter
import com.example.starwars.presentation.adapter.PeopleAdapter
import com.example.starwars.presentation.viewmodel.FilmViewModel
import com.example.starwars.presentation.viewmodel.PeopleViewModel
import com.example.starwars.presentation.viewmodel.PlanetViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter

class PeopleFragment : Fragment() {

    lateinit var peopleViewModel: PeopleViewModel
    private lateinit var binding : FragmentPeopleBinding
    lateinit var peopleAdapter : PeopleAdapter
    private var searchPeopleJob: Job? = null


    lateinit var planetViewModel: PlanetViewModel
    var urlPlanetList : MutableList<String> = mutableListOf()
    var planetList : MutableList<Planet> = mutableListOf()


    lateinit var filmViewModel: FilmViewModel
    var urlFilmList : MutableList<String> = mutableListOf()
    var filmList : MutableList<Film> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_people, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentPeopleBinding.bind(view)
        peopleViewModel = (activity as MainActivity).peopleViewModel
        planetViewModel = (activity as MainActivity).planetViewModel
        filmViewModel = (activity as MainActivity).filmViewModel
        peopleAdapter = (activity as MainActivity).peopleAdapter
        peopleAdapter.updatedListListener { people, index ->

        }

        initPeopleAdapter()
        searchPeople((activity as MainActivity).getPlanetValue(), (activity as MainActivity).getFilmValue())
        managePeopleList()
        filterListener()

        planetViewModel.planet.observe(viewLifecycleOwner, { modelResult ->
            peopleAdapter.snapshot().forEachIndexed { index, people ->
                people?.let {
                    if (people.planet == null && people.homeworld == modelResult.url){
                        people.planet = modelResult
                        peopleAdapter.notifyItemChanged(index)
                    }
                }
            }
            planetList.add(modelResult)
        })

        filmViewModel.film.observe(viewLifecycleOwner, { modelResult ->
            filmList.add(modelResult)
            peopleAdapter.snapshot().forEachIndexed { peopleIndex, people ->
                people?.let {
                    people.films.forEach { filmString ->
                        val movie = people.movies.find { it.url == modelResult.url }
                        if(filmString == modelResult.url && movie == null){
                            people.movies.add(modelResult)
                        }
                    }
                }
                peopleAdapter.notifyItemChanged(peopleIndex)
            }
        })


    }

    // People RV
    private fun initPeopleAdapter(){
        binding.rvPeople.layoutManager = LinearLayoutManager(activity)
        binding.rvPeople.adapter = peopleAdapter.withLoadStateHeaderAndFooter(
            header = LoadStateAdapter {peopleAdapter.retry()},
            footer = LoadStateAdapter{peopleAdapter.retry()}
        )

        peopleAdapter.addLoadStateListener { loadState ->
            val isPeopleListEmpty = loadState.refresh is LoadState.NotLoading && peopleAdapter.itemCount == 0
            showEmptyPeopleList(isPeopleListEmpty)

            binding.rvPeople.isVisible = loadState.source.refresh is LoadState.NotLoading
            binding.progressbar.isVisible = loadState.source.refresh is LoadState.Loading
            binding.retryBttn.isVisible = loadState.source.refresh is LoadState.Error

            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
            errorState?.let {
                Toast.makeText(
                    activity,
                    "\uD83D\uDE28 Wooops ${it.error}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun showEmptyPeopleList(isPeopleListEmpty : Boolean){
        if(isPeopleListEmpty){
            binding.emptyPeopleList.visibility = View.VISIBLE
            binding.rvPeople.visibility = View.GONE
        } else {
            binding.emptyPeopleList.visibility = View.GONE
            binding.rvPeople.visibility = View.VISIBLE
        }
    }

    private fun managePeopleList(){
        // Scroll to top when the list is refreshed from network.
        lifecycleScope.launch {
            peopleAdapter.loadStateFlow
                // Only emit when REFRESH LoadState for RemoteMediator changes.
                .distinctUntilChangedBy { it.refresh }
                // Only react to cases where Remote REFRESH completes i.e., NotLoading.
                .filter { it.refresh is LoadState.NotLoading }
                .collect { binding.rvPeople.scrollToPosition(0) }
        }

        lifecycleScope.launch {
            peopleAdapter.loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                .filter { it.refresh is LoadState.NotLoading }
                .collect { updatePeoplePlanet() }
        }


        lifecycleScope.launch {
            peopleAdapter.loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                .filter { it.refresh is LoadState.NotLoading }
                .collect { updatePeopleFilm() }
        }


    }

    fun filterListener(planet : Planet? = null, film : Film? = null){
        searchPeopleJob?.cancel()
        searchPeopleJob = lifecycleScope.launch {
            delay(1500)
            searchPeople(planet, film)
        }
    }

    private fun searchPeople(planet : Planet? = null, film : Film? = null){
        searchPeopleJob?.cancel()
        searchPeopleJob = lifecycleScope.launch {
            peopleViewModel.getPeople(planet?.id, film?.id).collectLatest {
                (activity as MainActivity).peopleAdapter.submitData(it)
            }
        }
    }

    private fun updatePeoplePlanet(){
        peopleAdapter.snapshot().forEachIndexed{ index, people ->
            people?.let {
                val addedUrl = urlPlanetList.find { it == people.homeworld }
                if(people.planet == null && addedUrl == null){
                    urlPlanetList.add(people.homeworld)
                    lifecycleScope.launch {
                        val id = people.homeworld.filter { it.isDigit() }
                        planetViewModel.getPlanet(id.toLong())
                    }
                } else {
                    val planet = planetList.find { it.url == people.homeworld }
                    planet?.let {
                        people.planet = it
                        peopleAdapter.notifyItemChanged(index)
                    }
                }

            }
        }
    }

    private fun updatePeopleFilm(){
        peopleAdapter.snapshot().forEachIndexed { peopleIndex, people ->
            people?.let {
                people.films.forEachIndexed { filmIndex, filmString ->
                    var addedUrl = urlFilmList.find { it == filmString }
                    if (addedUrl == null){
                        urlFilmList.add(filmString)
                        val id = filmString.filter { it.isDigit() }
                        filmViewModel.getFilm(id.toLong())
                    } else {
                        val film = filmList.find { it.url == filmString }
                        film?.let { film ->
                            var movie = people.movies.find { it.url == filmString }
                            if(movie == null){
                                people.movies.add(film)
                            }
                        }
                    }
                }
                peopleAdapter.notifyItemChanged(peopleIndex)
            }
        }
    }













}