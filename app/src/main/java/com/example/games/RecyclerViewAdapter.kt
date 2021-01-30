package com.example.games

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_game.view.*

class RecyclerViewAdapter(val VideoGamesList: List<VideoGame>): RecyclerView.Adapter<CustomViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        return CustomViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_game,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return VideoGamesList.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val currentGame = VideoGamesList[position]
        val releaseDate = "Released: ${currentGame.released}"
        holder.itemView.apply {
            tvVideoGameRating.text = currentGame.rating
            tvVideoGameReleaseDate.text = releaseDate
            tvVideoGameTitle.text = currentGame.name
            Picasso.get().load(currentGame.background_image).into(ivVideoGameImg) //picasso kullandık
        }
        holder.VideoGame = currentGame
    }
}

class CustomViewHolder(val view: View, var VideoGame: VideoGame? = null): RecyclerView.ViewHolder(view){
    init {
        view.setOnClickListener {
            val intent = Intent(view.context, GameActivity::class.java)
            intent.putExtra("id", VideoGame?.id) //pass gameId
            view.context.startActivity(intent)
        }
    }


}