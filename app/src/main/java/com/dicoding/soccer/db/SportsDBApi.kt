package com.dicoding.soccer.db

import com.dicoding.soccer.db.model.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SportsDBApi {
    @GET("eventspastleague.php")
    fun getPastEvent(@Query("id", encoded = true) leagueId: String): Call<MatchResponse>

    @GET("eventsnextleague.php")
    fun getNextEvent(@Query("id", encoded = true) leagueId: String): Call<MatchResponse>

    @GET("lookupevent.php")
    fun getEventDetail(@Query("id", encoded = true) eventId: String): Call<MatchDetailResponse>

    @GET("lookupteam.php")
    fun getTeamInfo(@Query("id", encoded = true) teamId: String): Call<TeamResponse>

    @GET("searchevents.php")
    fun srcEvent(@Query("e", encoded = true) teamName: String): Call<MatchSearchResponse>

    @GET("searchteams.php")
    fun srcTeam(@Query("t", encoded = true) teamName: String): Call<TeamResponse>

    @GET("lookupleague.php")
    fun getLeague(@Query("id", encoded = true) idLeague: String): Call<LeagueResponse>

    @GET("lookup_all_teams.php")
    fun getLeagueTeams(@Query("id", encoded = true) idLeague: String): Call<TeamResponse>

    @GET("lookuptable.php")
    fun seeLeagueTable(@Query("l", encoded = true) leagueId: String): Call<LeagueTableResponse>

    @GET("lookup_all_players.php")
    fun getPlayer(@Query("id", encoded = true) teamId: String): Call<PlayerResponse>

    @GET("lookupplayer.php")
    fun getPlayerDetail(@Query("id", encoded = true) playerId: String): Call<PlayerDetailResponse>
}