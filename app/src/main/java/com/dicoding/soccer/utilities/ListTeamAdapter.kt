package com.dicoding.soccer.utilities

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.soccer.R
import com.dicoding.soccer.db.model.Team
import com.dicoding.soccer.module.team.TeamInterface
import kotlinx.android.synthetic.main.team_item.view.*

class ListTeamAdapter(
    private val context: Context,
    private val team: List<Team>,
    private val teamInterface: TeamInterface,
    private val listener: (Team) -> Unit
) : RecyclerView.Adapter<ListTeamAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = Holder(LayoutInflater.from(context).inflate(R.layout.team_item, parent, false), teamInterface)

    override fun getItemCount(): Int = team.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bindItem(team[position], listener)
    }

    class Holder(view: View, private val ti: TeamInterface) : RecyclerView.ViewHolder(view) {

        fun bindItem(team: Team, listener: (Team) -> Unit){
            Glide.with(itemView)
                .load(team.teamBadge)
                .into(itemView.team_badge)

            itemView.team_name.text = team.teamName
            itemView.team_formed.text = team.teamFormedYear
            itemView.team_country.text = team.teamCountry

            itemView.team_row.setOnClickListener {
                listener(team)
                ti.whenItemSelected(adapterPosition)
            }
        }
    }
}