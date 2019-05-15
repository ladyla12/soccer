package com.dicoding.soccer.utilities

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.soccer.R
import com.dicoding.soccer.db.model.Player
import com.dicoding.soccer.module.player.PlayerInterface
import kotlinx.android.synthetic.main.grid_item.view.*

class ListPlayerAdapter(
    private val context: Context,
    private val player: List<Player>,
    private val playerInterface: PlayerInterface,
    private val listener: (Player) -> Unit
): RecyclerView.Adapter<ListPlayerAdapter.PlayerHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PlayerHolder(LayoutInflater.from(context).inflate(
        R.layout.grid_item, parent, false
    ), playerInterface)

    override fun getItemCount(): Int = player.size

    override fun onBindViewHolder(holder: PlayerHolder, position: Int) = holder.bindItem(player[position], listener)

    class PlayerHolder(view: View, private val pi: PlayerInterface): RecyclerView.ViewHolder(view) {

        fun bindItem(player: Player, listener: (Player) -> Unit){
            Glide.with(itemView)
                .load(player.strThumb)
                .into(itemView.img_item)

            itemView.textview_item.text = player.strPlayer

            itemView.grid_row.setOnClickListener {
                listener(player)
                pi.whenDataSelected(adapterPosition)
            }
        }

    }
}