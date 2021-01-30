package com.example.games

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import com.example.games.fragments.FavoritesFragment
import com.example.games.fragments.HomeFragment
import com.google.gson.GsonBuilder
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {
    val homeFragment = HomeFragment() //fragment init
    val favoritesFragment = FavoritesFragment() //fragment init

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fetchData()
        bottomNavigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.ic_home -> supportFragmentManager.beginTransaction().hide(favoritesFragment).show(homeFragment).commit()
                R.id.ic_favorite -> supportFragmentManager.beginTransaction().hide(homeFragment).show(favoritesFragment).commit()
            }
            true
        }

    }

    private fun fetchData() { // request and fragment data transfer
        val client = OkHttpClient()

        val request = Request.Builder()
            .url("https://rawg-video-games-database.p.rapidapi.com/games")
            .get()
            .addHeader("x-rapidapi-key", "13165636e9mshbb981a51ed09429p14ebcbjsn7ac6c8627ffe")
            .addHeader("x-rapidapi-host", "rawg-video-games-database.p.rapidapi.com")
            .build()

        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()
                val gson = GsonBuilder().create() //using gson
                val gamesList = gson.fromJson(body, VideoGamesList::class.java)
                val savedList = gamesList.results

                val bundle = Bundle()
                bundle.putParcelableArrayList("listOfGames", savedList)

                homeFragment.arguments = bundle
                favoritesFragment.arguments = bundle

                supportFragmentManager.beginTransaction().add(R.id.fl_wrapper,favoritesFragment,"2").hide(favoritesFragment).commit()
                supportFragmentManager.beginTransaction().add(R.id.fl_wrapper,homeFragment,"1").commit()
            }

            override fun onFailure(call: Call, e: IOException) {
                println("Failed to connect")
            }
        })
    }
}

class VideoGamesList(val results: ArrayList<VideoGame>)
@Parcelize
data class VideoGame(val id: Int, val name: String, val background_image: String, val rating: String, val released: String) : Parcelable