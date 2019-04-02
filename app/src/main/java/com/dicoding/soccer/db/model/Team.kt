package com.dicoding.soccer.db.model

import com.google.gson.annotations.SerializedName

data class Team (
    @SerializedName("idTeam")
    var teamId: String? = null,
    @SerializedName("intLoved")
    var teamFav: String? = null,
    @SerializedName("strTeam")
    var teamName: String? = null,
    @SerializedName("strDescriptionEN")
    var teamDesc: String? = null,
    @SerializedName("strTeamBadge")
    var teamBadge: String? = null
)