package com.example.starwars.presentation.view


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.starwars.R


class FilterFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = (activity as MainActivity)

        val t: FragmentTransaction = activity.supportFragmentManager.beginTransaction()
        val planetFragment = PlanetFragment()
        t.replace(R.id.planetFrameLayout, planetFragment)
        t.commit()

        val u: FragmentTransaction = activity.supportFragmentManager.beginTransaction()
        val filmFragment = FilmFragment()
        u.replace(R.id.filmFrameLayout, filmFragment)
        u.commit()

    }
}