package com.dicoding.soccer.utilities

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.soccer.R
import com.dicoding.soccer.db.model.Team
import com.dicoding.soccer.module.search.SearchInterface
import kotlinx.android.synthetic.main.grid_item.view.*

class SearchTeamAdapter(
    private val context: Context,
    private val team: List<Team>,
    private val teamInterface: SearchInterface,
    private val listener: (Team) -> Unit
) : RecyclerView.Adapter<SearchTeamAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        Holder(LayoutInflater.from(context).inflate(R.layout.grid_item, parent, false), teamInterface)

    override fun getItemCount(): Int = team.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bindItem(team[position], listener)
    }

    class Holder(view: View, private val ti: SearchInterface) : RecyclerView.ViewHolder(view) {

        fun bindItem(team: Team, listener: (Team) -> Unit) {
            Glide.with(itemView)
                .load(team.teamBadge)
                .into(itemView.img_item)

            itemView.textview_item.text = team.teamName
            itemView.grid_row.setOnClickListener {
                listener(team)
                ti.whenItemSelected(adapterPosition)
            }
        }
    }
}