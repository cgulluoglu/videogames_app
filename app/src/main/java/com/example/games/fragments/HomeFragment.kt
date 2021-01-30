package com.example.games.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.games.*
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment(R.layout.fragment_home) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvNotFound.visibility = View.GONE // make notFound textview not visible
        rvVideoGames.layoutManager = LinearLayoutManager(activity)

        val gamesList = arguments?.getParcelableArrayList<VideoGame>("listOfGames") //get list from MainActivity
        val gamesListNew = gamesList?.toList() // parcelable arraylist to list
        updateUI(gamesListNew!!)

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchGames(s.toString(), gamesListNew)
            }
        })
    }

    private fun updateUI(gamesList: List<VideoGame>) {
        activity?.runOnUiThread {
            rvVideoGames.adapter = RecyclerViewAdapter(gamesList.subList(3,gamesList.size)) //starting from index 3
            vpTopThree.adapter = ViewPagerAdapter(gamesList)
            circleIndicator.setViewPager(vpTopThree)
        }
    }

    fun searchGames(query : String, gamesList: List<VideoGame>){
        val newGameList : MutableList<VideoGame> = mutableListOf()
        if(query.length > 2){ //check string length
            vpTopThree.visibility = View.GONE //viewpager disable
            circleIndicator.visibility = View.GONE // circleindicator disable

            for (VideoGame in gamesList){
                if (VideoGame.name.contains(query, ignoreCase = true)) {
                    newGameList.add(VideoGame) //add to new list if satisfies search
                }
            }
            if(newGameList.isEmpty()){ //if search returns empty list
                tvNotFound.visibility = View.VISIBLE //not found visible
                vpTopThree.visibility = View.GONE
                circleIndicator.visibility = View.GONE
                rvVideoGames.visibility = View.GONE
            } else {
                tvNotFound.visibility = View.GONE
                activity?.runOnUiThread{
                    rvVideoGames.adapter = RecyclerViewAdapter(newGameList)
                }
            }

        } else { // if query is too short
            tvNotFound.visibility = View.GONE
            vpTopThree.visibility = View.VISIBLE
            circleIndicator.visibility = View.VISIBLE
            rvVideoGames.visibility = View.VISIBLE
            activity?.runOnUiThread{
                rvVideoGames.adapter = RecyclerViewAdapter(gamesList.subList(3,gamesList.size))
            }

        }
    }
}
