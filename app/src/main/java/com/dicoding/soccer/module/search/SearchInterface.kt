package com.dicoding.soccer.module.search

import com.dicoding.soccer.db.model.MatchSearchResponse
import com.dicoding.soccer.db.model.TeamResponse

interface SearchInterface {
    fun showLoading()
    fun hideLoading()
    fun showMessage(msg: String)
    fun searchMatchResult(data: MatchSearchResponse)
    fun searchTeamResult(data: TeamResponse)
    fun whenItemSelected(position: Int)
}