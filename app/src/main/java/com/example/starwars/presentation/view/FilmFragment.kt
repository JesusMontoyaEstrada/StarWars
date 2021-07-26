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
import com.example.starwars.databinding.FragmentFilmBinding
import com.example.starwars.presentation.adapter.FilmAdapter
import com.example.starwars.presentation.adapter.LoadStateAdapter
import com.example.starwars.presentation.viewmodel.FilmViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

class FilmFragment : Fragment() {

    lateinit var filmViewModel : FilmViewModel
    private var searchFilmJob : Job? = null
    private lateinit var binding: FragmentFilmBinding
    lateinit var filmAdapter: FilmAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_film, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentFilmBinding.bind(view)
        filmViewModel = (activity as MainActivity).filmViewModel
        filmAdapter = (activity as MainActivity).filmAdapter
        filmAdapter.setOnClickListener {
            (activity as MainActivity).setFilmValue(it)
        }

        initFilmAdapter()
        manageFilmList()
        searchFilm()
    }

    private fun initFilmAdapter(){
        binding.rvFilms.layoutManager = LinearLayoutManager(activity)
        binding.rvFilms.adapter = filmAdapter.withLoadStateHeaderAndFooter(
            header = LoadStateAdapter {filmAdapter.retry()},
            footer = LoadStateAdapter{filmAdapter.retry()}
        )

        filmAdapter.addLoadStateListener { loadState ->
            val isFilmListEmpty = loadState.refresh is LoadState.NotLoading && filmAdapter.itemCount == 0
            showEmptyFilmList(isFilmListEmpty)

            binding.rvFilms.isVisible = loadState.source.refresh is LoadState.NotLoading
            binding.rvTitle.isVisible = loadState.source.refresh is LoadState.NotLoading
            binding.filmProgressBar.isVisible = loadState.source.refresh is LoadState.Loading
            binding.filmRetryButton.isVisible = loadState.source.refresh is LoadState.Error

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

    private fun manageFilmList(){
        // Scroll to top when the list is refreshed from network.
        lifecycleScope.launch {
            filmAdapter.loadStateFlow
                // Only emit when REFRESH LoadState for RemoteMediator changes.
                .distinctUntilChangedBy { it.refresh }
                // Only react to cases where Remote REFRESH completes i.e., NotLoading.
                .filter { it.refresh is LoadState.NotLoading }
                .collect { binding.rvFilms.scrollToPosition(0) }
        }
    }

    private fun showEmptyFilmList(isFilmListEmpty : Boolean){
        if(isFilmListEmpty){
            binding.rvTitle.visibility = View.INVISIBLE
            binding.emptyFilmList.visibility = View.VISIBLE
            binding.rvFilms.visibility = View.GONE
        } else {
            binding.emptyFilmList.visibility = View.GONE
            binding.rvTitle.visibility = View.VISIBLE
            binding.rvFilms.visibility = View.VISIBLE
        }
    }

    private fun searchFilm(){
        searchFilmJob?.cancel()
        searchFilmJob = lifecycleScope.launch {
            filmViewModel.getFilms().collectLatest {
//                filmAdapter.submitData(it)
                (activity as MainActivity).filmAdapter.submitData(it)
            }
        }
    }
}