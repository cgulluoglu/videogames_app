package com.example.games

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_game.*
import okhttp3.*
import java.io.IOException


class GameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        val gameId = intent.getIntExtra("id", 0) //getting gameId from Intent
        fetchSinglePageData(gameId) // request from api with gameId

        val sharedPref = getSharedPreferences("myPrefs",Context.MODE_PRIVATE)
        updateFavButton(gameId,sharedPref) // check if favorite

        btnFav.setOnClickListener {
            addOrRemoveFromFavorites(gameId,sharedPref)
        }
    }
    private fun updateFavButton(gameId: Int, sharedPref: SharedPreferences){ //update fav button
        if(sharedPref.contains(gameId.toString())){
            btnFav.setBackgroundResource(R.drawable.ic_baseline_thumb_up_24_selected)
        }
    }

    private fun addOrRemoveFromFavorites(gameId: Int, sharedPref: SharedPreferences) { //update sharedPref on button click
        val editor = sharedPref.edit()
        if(sharedPref.contains(gameId.toString())){
            editor.remove(gameId.toString())
            btnFav.setBackgroundResource(R.drawable.ic_baseline_thumb_up_24_shadow)
        } else {
            editor.putInt(gameId.toString(), gameId)
            btnFav.setBackgroundResource(R.drawable.ic_baseline_thumb_up_24_selected)
        }
        editor.apply()
    }

    private fun fetchSinglePageData(gameId : Int) { //fetches single page data
        val client = OkHttpClient()

        val request = Request.Builder()
            .url("https://rawg-video-games-database.p.rapidapi.com/games/$gameId")
            .get()
            .addHeader("x-rapidapi-key", "13165636e9mshbb981a51ed09429p14ebcbjsn7ac6c8627ffe")
            .addHeader("x-rapidapi-host", "rawg-video-games-database.p.rapidapi.com")
            .build()
        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()
                val gson = GsonBuilder().create()
                val gameInfo = gson.fromJson(body, SingleVideoGame::class.java)

                val regex = Regex("<[a-z /]*>")
                val regexApos = Regex("&#39;")

                val tempString = regex.replace(gameInfo.description, "") //<> tags removed
                val resultString = regexApos.replace(tempString,"'") // &#39; -> " ' "

                val metacritic = "Metacricitc: ${gameInfo.metacritic}"
                val released = "Released: ${gameInfo.released}"

                runOnUiThread {
                    tvTitleSinglePage.text = gameInfo.name
                    tvRatingSinglePage.text = metacritic
                    tvReleaseDateSinglePage.text = released
                    tvDescSinglePage.text = resultString
                    Picasso.get().load(gameInfo.background_image).into(ivMainImageSinglePage)
                }
            }
            override fun onFailure(call: Call, e: IOException) {
                println("Failed to connect")
            }
        })
    }
}
class SingleVideoGame(val id: Int, val name: String, val background_image: String, val metacritic: String, val released: String, val description: String)