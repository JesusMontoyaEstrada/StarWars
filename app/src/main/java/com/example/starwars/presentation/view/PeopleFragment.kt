package com.example.starwars.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.liveData
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.starwars.R
import com.example.starwars.data.model.Film
import com.example.starwars.data.model.People
import com.example.starwars.data.model.Planet
import com.example.starwars.databinding.FragmentPeopleBinding
import com.example.starwars.presentation.adapter.LoadStateAdapter
import com.example.starwars.presentation.adapter.PeopleAdapter
import com.example.starwars.presentation.viewmodel.PeopleViewModel
import com.example.starwars.presentation.viewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PeopleFragment : Fragment() {

    private val peopleViewModel: PeopleViewModel by viewModels()
    private val sharedViewModels: SharedViewModel by activityViewModels()
    private lateinit var binding : FragmentPeopleBinding
    private var peopleAdapter : PeopleAdapter = PeopleAdapter()
    private var searchPeopleJob: Job? = null

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

        initPeopleAdapter()
        managePeopleList()
        filterListener()

        sharedViewModels.filters.observe(viewLifecycleOwner, {
            searchPeople(it.first, it.second)
        })

        binding.retryBttn.setOnClickListener {
            searchPeople(sharedViewModels.filters.value?.first, sharedViewModels.filters.value?.second)
        }

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
                peopleAdapter.submitData(it)
            }
        }
    }
}