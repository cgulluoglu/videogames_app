package com.example.games.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.games.R
import com.example.games.RecyclerViewAdapter
import com.example.games.VideoGame
import kotlinx.android.synthetic.main.fragment_favorites.*

class FavoritesFragment : Fragment(R.layout.fragment_favorites) {
    private var shouldRefreshOnResume = false // var to refresh favorites tab on resume

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gamesList = arguments?.getParcelableArrayList<VideoGame>("listOfGames")
        val gamesListNew = gamesList?.toList()

        getFavorites(gamesListNew)
        rvVideoGames.layoutManager = LinearLayoutManager(activity)

    }
    override fun onResume() {
        super.onResume()
        if (shouldRefreshOnResume) {
            val gamesList = arguments?.getParcelableArrayList<VideoGame>("listOfGames")
            val gamesListNew = gamesList?.toList()
            getFavorites(gamesListNew)
        }
    }

    override fun onStop() {
        super.onStop()
        shouldRefreshOnResume = true
    }

    private fun getFavorites(gamesListNew: List<VideoGame>?) { //check favorites with shared pref and update recyclerview
        val favGamesList : MutableList<VideoGame> = mutableListOf()
        val sharedPref = activity!!.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        for(games in gamesListNew!!){
            if( sharedPref.contains(games.id.toString())){
                favGamesList.add(games)
            }
        }
        activity?.runOnUiThread {
            rvVideoGames.adapter = RecyclerViewAdapter(favGamesList)
        }
    }
}