package com.dicoding.soccer.db.model

import com.google.gson.annotations.SerializedName

data class PlayerResponse(

	@field:SerializedName("player")
	val player: List<Player>
)