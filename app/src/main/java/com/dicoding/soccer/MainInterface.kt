package com.dicoding.soccer

import com.dicoding.soccer.db.model.LeagueResponse

interface MainInterface {
    fun showMessage(string: String)
    fun showLoading()
    fun hideLoading()
    fun loadLeague(data: LeagueResponse)
}