package com.example.starwars.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.starwars.data.model.Planet
import com.example.starwars.databinding.FilmViewItemBinding

class PlanetAdapter : PagingDataAdapter<Planet, PlanetViewHolder>(PLANET_COMPARATOR) {

    override fun onBindViewHolder(holder: PlanetViewHolder, position: Int) {
        val characterItem = getItem(position)
        if(characterItem != null){
            holder.bind(characterItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanetViewHolder {
        return PlanetViewHolder.create(parent)
    }

    companion object {
        private val PLANET_COMPARATOR = object : DiffUtil.ItemCallback<Planet>() {
            override fun areItemsTheSame(oldItem: Planet, newItem: Planet): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Planet, newItem: Planet): Boolean =
                oldItem == newItem
        }

        var onItemClickListener : ((Planet)-> Unit) ? = null
    }

    fun setOnClickListener(listener : (Planet) -> Unit){
        onItemClickListener = listener
    }
}


class PlanetViewHolder(val binding : FilmViewItemBinding): RecyclerView.ViewHolder(binding.root) {

    private var planet : Planet? = null

    fun bind(planet: Planet?){
        if(planet == null){
            binding.textView.text = "Cargando"
        } else {
            showPlanet(planet)
        }
    }

    private fun showPlanet(planet : Planet){
        this.planet = planet
        binding.textView.text = planet.name

        binding.root.setOnClickListener {
            PlanetAdapter.onItemClickListener?.let {
                it(planet)
            }
        }
    }

    companion object {
        fun create(parent: ViewGroup): PlanetViewHolder {
            val binding = FilmViewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return PlanetViewHolder(binding)
        }
    }
}