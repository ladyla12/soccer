package com.dicoding.soccer.utilities

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.soccer.R
import com.dicoding.soccer.db.model.LeagueTable
import kotlinx.android.synthetic.main.klasemen_item.view.*

class ListKlasemenAdapter(
    private val context: Context,
    private val klasemen: List<LeagueTable>
) : RecyclerView.Adapter<ListKlasemenAdapter.KlasemenHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = KlasemenHolder(LayoutInflater.from(context).inflate(R.layout.klasemen_item, parent, false))

    override fun getItemCount(): Int = klasemen.size

    override fun onBindViewHolder(holder: KlasemenHolder, position: Int) {
        holder.bindItem(klasemen[position])
    }

    class KlasemenHolder(view: View): RecyclerView.ViewHolder(view) {
        fun bindItem(klasemen: LeagueTable) {
            itemView.klasement_team_name.text = klasemen.name
            itemView.klasement_played.text = klasemen.played.toString()
            itemView.klasement_goalfor.text = klasemen.goalsfor.toString()
            itemView.klasement_win.text = klasemen.win.toString()
            itemView.klasement_draw.text = klasemen.draw.toString()
            itemView.klasement_loss.text = klasemen.loss.toString()
            itemView.klasement_total.text = klasemen.total.toString()
        }
    }
}