package com.dicoding.soccer.db.model

import com.google.gson.annotations.SerializedName

data class Team (
    @SerializedName("idTeam")
    var teamId: String? = null,
    @SerializedName("strTeam")
    var teamName: String? = null,
    @SerializedName("intFormedYear")
    var teamFormedYear: String? = null,
    @SerializedName("strCountry")
    var teamCountry: String? = null,
    @SerializedName("strDescriptionEN")
    var teamDesc: String? = null,
    @SerializedName("strTeamBadge")
    var teamBadge: String? = null,
    @SerializedName("strFacebook")
    var teamFacebook: String? = null,
    @SerializedName("strTwitter")
    var teamTwitter: String? = null,
    @SerializedName("strYoutube")
    var teamYoutube: String? = null
)