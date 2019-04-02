package com.dicoding.soccer.utilities

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.soccer.R
import com.dicoding.soccer.module.favorite.FavInterface
import kotlinx.android.synthetic.main.list_match.view.*

class FavMatchAdapter(
    private val context: Context,
    private val favList: List<Favorite>,
    private val favInterface: FavInterface,
    private val listener: (Favorite) -> Unit
) : RecyclerView.Adapter<FavMatchAdapter.FavHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        FavHolder(LayoutInflater.from(context).inflate(R.layout.list_match, parent, false), favInterface)

    override fun getItemCount(): Int = favList.size

    override fun onBindViewHolder(holder: FavHolder, position: Int) {
        holder.bindItem(favList[position], listener)
    }

    class FavHolder(view: View, private val fi: FavInterface) : RecyclerView.ViewHolder(view) {
        fun bindItem(favList: Favorite, listener: (Favorite) -> Unit){
            itemView.tgl_match.text = favList.matchDate
            itemView.home_team_name.text = favList.homeTeamName
            itemView.home_score.text = favList.homeTeamScore
            itemView.away_team_name.text = favList.awayTeamName
            itemView.away_score.text = favList.awayTeamScore
            itemView.row_match.setOnClickListener {
                listener(favList)
                fi.whenItemSelected(adapterPosition)
            }
        }
    }

}