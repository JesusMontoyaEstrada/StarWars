package com.example.starwars.presentation.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.starwars.R
import com.example.starwars.databinding.FragmentPlanetBinding
import com.example.starwars.presentation.adapter.LoadStateAdapter
import com.example.starwars.presentation.adapter.PlanetAdapter
import com.example.starwars.presentation.viewmodel.PlanetViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PlanetFragment : Fragment() {

    private val planetViewModel : PlanetViewModel by viewModels()
    private var searchPlanetJob : Job? = null
    private lateinit var binding: FragmentPlanetBinding
    private var planetAdapter: PlanetAdapter = PlanetAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_planet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentPlanetBinding.bind(view)
        planetAdapter.setOnClickListener {
            (activity as MainActivity).setPlanetValue(it)
        }

        initPlanetAdapter()
        managePlanetList()
        searchPlanet()
    }

    //Planet RV
    private fun initPlanetAdapter(){
        binding.rvPlanets.layoutManager = LinearLayoutManager(activity)
        binding.rvPlanets.adapter = planetAdapter.withLoadStateHeaderAndFooter(
            header = LoadStateAdapter {planetAdapter.retry()},
            footer = LoadStateAdapter{planetAdapter.retry()}
        )

        planetAdapter.addLoadStateListener { loadState ->
            val isPlanetListEmpty = loadState.refresh is LoadState.NotLoading && planetAdapter.itemCount == 0
            showEmptyPlanetList(isPlanetListEmpty)

            binding.rvPlanets.isVisible = loadState.source.refresh is LoadState.NotLoading
            binding.rvPlanetTitle.isVisible = loadState.source.refresh is LoadState.NotLoading
            binding.planetProgressBar.isVisible = loadState.source.refresh is LoadState.Loading
            binding.planetRetryButton.isVisible = loadState.source.refresh is LoadState.Error

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

    private fun managePlanetList(){
        // Scroll to top when the list is refreshed from network.
        lifecycleScope.launch {
            planetAdapter.loadStateFlow
                // Only emit when REFRESH LoadState for RemoteMediator changes.
                .distinctUntilChangedBy { it.refresh }
                // Only react to cases where Remote REFRESH completes i.e., NotLoading.
                .filter { it.refresh is LoadState.NotLoading }
                .collect { binding.rvPlanets.scrollToPosition(0) }
        }
    }

    private fun showEmptyPlanetList(isPlanetListEmpty : Boolean){
        if(isPlanetListEmpty){
            binding.rvPlanetTitle.visibility = View.INVISIBLE
            binding.emptyPlanetList.visibility = View.VISIBLE
            binding.rvPlanets.visibility = View.GONE
        } else {
            binding.emptyPlanetList.visibility = View.GONE
            binding.rvPlanetTitle.visibility = View.VISIBLE
            binding.rvPlanets.visibility = View.VISIBLE
        }
    }

    private fun searchPlanet(){
        searchPlanetJob?.cancel()
        searchPlanetJob = lifecycleScope.launch {
            planetViewModel.getPlanets().collectLatest {
                planetAdapter.submitData(it)
            }
        }
    }
}