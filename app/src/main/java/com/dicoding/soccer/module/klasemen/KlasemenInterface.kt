package com.dicoding.soccer.module.klasemen

import com.dicoding.soccer.db.model.LeagueTableResponse

interface KlasemenInterface {
    fun showLoading()
    fun hideLoading()
    fun showMessage(message: String)
    fun loadData(data: LeagueTableResponse)
}