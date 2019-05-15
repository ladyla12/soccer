package com.dicoding.soccer.db.model
import com.google.gson.annotations.SerializedName

class LeagueTable(

	@field:SerializedName("loss")
	val loss: Int? = null,

	@field:SerializedName("total")
	val total: Int? = null,

	@field:SerializedName("goalsfor")
	val goalsfor: Int? = null,

	@field:SerializedName("teamid")
	val teamid: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("draw")
	val draw: Int? = null,

	@field:SerializedName("played")
	val played: Int? = null,

	@field:SerializedName("win")
	val win: Int? = null
)