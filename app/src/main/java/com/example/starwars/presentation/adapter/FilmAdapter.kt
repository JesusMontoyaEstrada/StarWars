package com.example.starwars.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.starwars.data.model.Film
import com.example.starwars.databinding.FilmViewItemBinding

class FilmAdapter : PagingDataAdapter<Film, FilmViewHolder>(FILM_COMPARATOR) {

    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
        val characterItem = getItem(position)
        if(characterItem != null){
            holder.bind(characterItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmViewHolder {
        return FilmViewHolder.create(parent)
    }

    companion object {
        private val FILM_COMPARATOR = object : DiffUtil.ItemCallback<Film>() {
            override fun areItemsTheSame(oldItem: Film, newItem: Film): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Film, newItem: Film): Boolean =
                oldItem == newItem
        }

        var onItemClickListener : ((Film)-> Unit) ? = null
    }

    fun setOnClickListener(listener : (Film) -> Unit){
        onItemClickListener = listener
    }
}


class FilmViewHolder(val binding : FilmViewItemBinding): RecyclerView.ViewHolder(binding.root) {

    private var film : Film? = null

    fun bind(film: Film?){
        if(film == null){
            binding.textView.text = "Cargando"
        } else {
            showFilm(film)
        }
    }

    private fun showFilm(film : Film){
        this.film = film
        binding.textView.text = film.name

        binding.root.setOnClickListener {
            FilmAdapter.onItemClickListener?.let {
                it(film)
            }
        }
    }

    companion object {
        fun create(parent: ViewGroup): FilmViewHolder {
            val binding = FilmViewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return FilmViewHolder(binding)
        }
    }
}
