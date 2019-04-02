package com.dicoding.soccer.db.repository

import com.dicoding.soccer.db.RestApiClient
import com.dicoding.soccer.db.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface ApiCallback<T> {
    fun onLoad(data: T?)
    fun onError(error: String?)
}

class ApiRepository {

    fun getPastEvent(leagueId: String, callback: ApiCallback<MatchResponse?>) {
        RestApiClient.create().getPastEvent(leagueId)
            .enqueue(object : Callback<MatchResponse> {
                override fun onResponse(call: Call<MatchResponse>, response: Response<MatchResponse>) {
                    response.let {
                        if (it.isSuccessful) {
                            callback.onLoad(it.body())
                        } else {
                            callback.onError(response.errorBody().toString())
                        }
                    }
                }

                override fun onFailure(call: Call<MatchResponse>, t: Throwable) {
                    callback.onError(t.message)
                }
            })
    }

    fun getNextEvent(eventId: String, callback: ApiCallback<MatchResponse?>) {
        RestApiClient.create().getNextEvent(eventId)
            .enqueue(object : Callback<MatchResponse> {
                override fun onResponse(call: Call<MatchResponse>, response: Response<MatchResponse>) {
                    response.let {
                        if (it.isSuccessful) {
                            callback.onLoad(it.body())
                        } else {
                            callback.onError(response.errorBody().toString())
                        }
                    }
                }

                override fun onFailure(call: Call<MatchResponse>, t: Throwable) {
                    callback.onError(t.message)
                }
            })
    }

    fun getDetailMatch(eventId: String, callback: ApiCallback<MatchDetailResponse?>) {
        RestApiClient.create().getEventDetail(eventId)
            .enqueue(object : Callback<MatchDetailResponse>{
                override fun onResponse(call: Call<MatchDetailResponse>, response: Response<MatchDetailResponse>) {
                    response.let {
                        if (it.isSuccessful){
                            callback.onLoad(it.body())
                        }
                        else {
                            callback.onError(response.errorBody().toString())
                        }
                    }
                }

                override fun onFailure(call: Call<MatchDetailResponse>, t: Throwable) {
                    callback.onError(t.message)
                }
            })
    }

    fun getDetailTeam(teamId: String, callback: ApiCallback<TeamResponse?>) {
        RestApiClient.create().getTeamInfo(teamId)
            .enqueue(object : Callback<TeamResponse>{
                override fun onResponse(call: Call<TeamResponse>, response: Response<TeamResponse>) {
                    response.let {
                        if (it.isSuccessful){
                            callback.onLoad(it.body())
                        }
                        else {
                            callback.onError(response.errorBody().toString())
                        }
                    }
                }

                override fun onFailure(call: Call<TeamResponse>, t: Throwable) {
                    callback.onError(t.message)
                }
            })
    }

    fun getLeague(leagueId: String, callback: ApiCallback<LeagueResponse?>){
        RestApiClient.create().getLeague(leagueId)
            .enqueue(object : Callback<LeagueResponse>{
                override fun onResponse(call: Call<LeagueResponse>, response: Response<LeagueResponse>) {
                    response.let {
                        if (it.isSuccessful){
                            callback.onLoad(it.body())
                        }
                        else {
                            callback.onError(response.errorBody().toString())
                        }
                    }
                }

                override fun onFailure(call: Call<LeagueResponse>, t: Throwable) {
                    callback.onError(t.message)
                }
            })
    }

    fun searchEvent(matchName: String, callback: ApiCallback<SearchResponse?>) {
        RestApiClient.create().srcEvent(matchName)
            .enqueue(object : Callback<SearchResponse> {
                override fun onResponse(call: Call<SearchResponse>, response: Response<SearchResponse>) {
                    response.let {
                        if (it.isSuccessful) {
                            callback.onLoad(it.body())
                        } else {
                            callback.onError(response.errorBody().toString())
                        }
                    }
                }

                override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                    callback.onError(t.message)
                }
            })
    }

}