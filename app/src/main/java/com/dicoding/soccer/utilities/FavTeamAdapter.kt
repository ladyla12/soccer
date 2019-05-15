package com.dicoding.soccer.utilities

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.soccer.R
import com.dicoding.soccer.module.favorite.FavInterface
import kotlinx.android.synthetic.main.grid_item.view.*

class FavTeamAdapter(
    private val context: Context,
    private val favList: List<FavoriteTeam>,
    private val favInterface: FavInterface,
    private val listener: (FavoriteTeam) -> Unit
) : RecyclerView.Adapter<FavTeamAdapter.FavHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        FavHolder(LayoutInflater.from(context).inflate(R.layout.grid_item, parent, false), favInterface)

    override fun getItemCount(): Int = favList.size

    override fun onBindViewHolder(holder: FavHolder, position: Int) = holder.bindItem(favList[position], listener)

    class FavHolder(view: View, private val fi: FavInterface) : RecyclerView.ViewHolder(view) {
        fun bindItem(favList: FavoriteTeam, listener: (FavoriteTeam) -> Unit) {
            Glide.with(itemView)
                .load(favList.teamBadge)
                .into(itemView.img_item)

            itemView.textview_item.text = favList.teamName
            itemView.grid_row.setOnClickListener {
                listener(favList)
                fi.whenItemSelected(adapterPosition)
            }
        }
    }
}