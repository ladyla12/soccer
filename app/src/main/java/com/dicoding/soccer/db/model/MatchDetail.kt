package com.dicoding.soccer.db.model

import com.google.gson.annotations.SerializedName

data class MatchDetail (
    @SerializedName("idEvent")
    var eventId: String? = null,

    // Home
    @SerializedName("strHomeTeam")
    var homeTeam: String? = null,

    @SerializedName("idHomeTeam")
    var homeTeamId: String? = null,

    @SerializedName("intHomeScore")
    var homeScore: String? = null,

    @SerializedName("strHomeGoalDetails")
    var homeGoalDetails: String? = null,

    @SerializedName("strHomeLineupGoalkeeper")
    var homeLineupGoalkeeper: String? = null,

    @SerializedName("strHomeLineupDefense")
    var homeLineupDefense: String? = null,

    @SerializedName("strHomeLineupMidfield")
    var homeLineupMidfield: String? = null,

    @SerializedName("strHomeLineupForward")
    var homeLineupForward: String? = null,

    @SerializedName("strHomeLineupSubstitutes")
    var homeLineupSubstitutes: String? = null,

    @SerializedName("strHomeFormation")
    var homeFormation: String? = null,

    @SerializedName("intHomeShots")
    var homeShots: String? = null,

    // Away
    @SerializedName("strAwayTeam")
    var awayTeam: String? = null,

    @SerializedName("idAwayTeam")
    var awayTeamId: String? = null,

    @SerializedName("intAwayScore")
    var awayScore: String? = null,

    @SerializedName("strAwayGoalDetails")
    var awayGoalDetails: String? = null,
    @SerializedName("strAwayLineupGoalkeeper")
    var awayLineupGoalkeeper: String? = null,

    @SerializedName("strAwayLineupDefense")
    var awayLineupDefense: String? = null,

    @SerializedName("strAwayLineupMidfield")
    var awayLineupMidfield: String? = null,

    @SerializedName("strAwayLineupForward")
    var awayLineupForward: String? = null,

    @SerializedName("strAwayLineupSubstitutes")
    var awayLineupSubstitutes: String? = null,

    @SerializedName("strAwayFormation")
    var awayFormation: String? = null,

    @SerializedName("intAwayShots")
    var awayShots: String? = null,

    @SerializedName("dateEvent")
    var eventDate: String? = null,

    @SerializedName("strTime")
    var eventTime: String? = null
)