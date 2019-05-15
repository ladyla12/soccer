package com.dicoding.soccer.module.player

import com.dicoding.soccer.db.model.PlayerDetailResponse
import com.dicoding.soccer.db.model.PlayerResponse

interface PlayerInterface {
    fun showLoading()
    fun hideLoading()
    fun showMessage(msg: String)
    fun loadData(data: PlayerResponse)
    fun loadDetail(data: PlayerDetailResponse)
    fun whenDataSelected(position: Int)
}