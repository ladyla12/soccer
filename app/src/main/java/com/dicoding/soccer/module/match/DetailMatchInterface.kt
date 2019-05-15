package com.dicoding.soccer.module.match

import com.dicoding.soccer.db.model.MatchDetailResponse
import com.dicoding.soccer.db.model.TeamResponse

interface DetailMatchInterface {
    fun showLoading()
    fun hideLoading()
    fun showMessage(msg: String)
    fun loadData(data: MatchDetailResponse)
    fun loadImage(data: TeamResponse, whichTeam: String)
}