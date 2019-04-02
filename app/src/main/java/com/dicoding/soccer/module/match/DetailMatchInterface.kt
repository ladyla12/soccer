package com.dicoding.soccer.module.match

import com.dicoding.soccer.db.model.MatchDetail
import com.dicoding.soccer.db.model.Team

interface DetailMatchInterface {
    fun showLoading()
    fun hideLoading()
    fun showMessage(msg: String)
    fun loadData(data: List<MatchDetail>)
    fun loadImage(data: List<Team>, whichTeam: String)
}