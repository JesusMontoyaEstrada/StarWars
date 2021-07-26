package com.example.starwars.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.starwars.data.model.Film
import com.example.starwars.data.model.People
import com.example.starwars.databinding.PeopleItemViewBinding

class PeopleAdapter : PagingDataAdapter<People, PeopleViewHolder>(PEOPLE_COMPARATOR) {

    override fun onBindViewHolder(holder: PeopleViewHolder, position: Int) {
        val characterItem = getItem(position)
        if(characterItem != null){
            holder.bind(characterItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeopleViewHolder {
        return PeopleViewHolder.create(parent)
    }


    companion object {
        private val PEOPLE_COMPARATOR = object : DiffUtil.ItemCallback<People>() {
            override fun areItemsTheSame(oldItem: People, newItem: People): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: People, newItem: People): Boolean =
                oldItem == newItem
        }

        var updatedList : ((People, Int)-> Unit) ? = null
    }

    fun updatedListListener(listener : (People, Int) -> Unit){
        updatedList = listener
    }
}


class PeopleViewHolder(private val binding : PeopleItemViewBinding): RecyclerView.ViewHolder(binding.root) {

    private var people : People? = null

    fun bind(people: People?){
        if(people == null){
            binding.tvName.text = "Cargando"
            binding.tvGen.text = "Cargando"
            binding.tvPlanet.text = "Cargando"
            binding.tvMovies.text = "Cargando"
        } else {
            showPeople(people)
        }
    }

    private fun showPeople(people : People){
        this.people = people
        binding.tvName.text = people.name
        binding.tvGen.text = people.gender
        binding.tvPlanet.text = people.planet?.name
        var movies = ""
        binding.tvMovies.text = ""

        people.movies.forEach { film ->
            movies += film.name+"\n"
        }

        binding.tvMovies.text = movies

        PeopleAdapter.updatedList?.let {

        }


    }

    companion object {
        fun create(parent: ViewGroup): PeopleViewHolder {
            val binding = PeopleItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return PeopleViewHolder(binding)
        }
    }
}