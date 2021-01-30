package com.example.games

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_page.view.*

class ViewPagerAdapter(val VideoGamesList: List<VideoGame>?) : RecyclerView.Adapter<ViewPagerAdapter.Pager2ViewHolder>(){

    inner class Pager2ViewHolder(itemView: View, var VideoGame: VideoGame? = null) : RecyclerView.ViewHolder(itemView){
        init {
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, GameActivity::class.java)
                intent.putExtra("id", VideoGame?.id)
                itemView.context.startActivity(intent)
            }
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewPagerAdapter.Pager2ViewHolder {
        return Pager2ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_page, parent, false))
    }

    override fun getItemCount(): Int {
        return 3
    }

    override fun onBindViewHolder(holder: ViewPagerAdapter.Pager2ViewHolder, position: Int) {
        val currentGame = VideoGamesList?.get(position)
        holder.itemView.apply {
            Picasso.get().load(currentGame?.background_image).into(ivTopThree); //picasso kullandık
            tvVideoGameTitle.text = currentGame?.name
        }
        holder.VideoGame = currentGame
    }
}