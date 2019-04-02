package com.dicoding.soccer.db.model

import com.google.gson.annotations.SerializedName

class League (
    @SerializedName("idLeague")
    var leagueId: String? = null,
    @SerializedName("strLeague")
    var leagueName: String? = null,
    @SerializedName("strFacebook")
    var leagueFacebook: String? = null,
    @SerializedName("strTwitter")
    var leagueTwitter: String? = null,
    @SerializedName("strYoutube")
    var leagueYoutube: String? = null,
    @SerializedName("strBadge")
    var leagueBadge: String? = null
)