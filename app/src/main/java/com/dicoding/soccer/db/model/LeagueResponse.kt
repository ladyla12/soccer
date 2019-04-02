package com.dicoding.soccer.db.model

import com.google.gson.annotations.SerializedName

class LeagueResponse(@field:SerializedName("leagues") val leagues: List<League>)