package com.dicoding.soccer.module.team

import com.dicoding.soccer.db.model.TeamResponse

interface TeamInterface {
    fun showLoading()
    fun hideLoading()
    fun showMessage(msg: String)
    fun loadData(data: TeamResponse)
    fun whenItemSelected(position: Int)
}