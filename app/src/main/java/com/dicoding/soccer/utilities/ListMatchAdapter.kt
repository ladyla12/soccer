package com.dicoding.soccer.utilities

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.soccer.R
import com.dicoding.soccer.db.model.Match
import com.dicoding.soccer.module.match.MatchInterface
import kotlinx.android.synthetic.main.list_match.view.*

class ListMatchAdapter(
    private val context: Context,
    private val match: List<Match>,
    private val matchInterface: MatchInterface,
    private val listener: (Match) -> Unit
) :
    RecyclerView.Adapter<ListMatchAdapter.MatchHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MatchHolder(LayoutInflater.from(context).inflate(R.layout.list_match, parent, false), matchInterface)

    override fun getItemCount(): Int = match.size

    override fun onBindViewHolder(holder: MatchHolder, position: Int) {
        holder.bindItem(match[position], listener)
    }

    class MatchHolder(view: View, private val mi: MatchInterface) : RecyclerView.ViewHolder(view) {

        fun bindItem(match: Match, listener: (Match) -> Unit) {
            itemView.tgl_match.text = dateFormat(match.eventDate)
            itemView.home_team_name.text = match.homeTeam
            itemView.home_score.text = match.homeScore
            itemView.away_team_name.text = match.awayTeam
            itemView.away_score.text = match.awayScore
            itemView.row_match.setOnClickListener {
                listener(match)
                mi.whenItemSelected(adapterPosition)
            }
        }
    }
}