package com.dicoding.soccer.module.match

import com.dicoding.soccer.db.model.MatchResponse

interface MatchInterface {
        fun showLoading()
        fun hideLoading()
        fun showMessage(msg: String)
        fun loadData(data: MatchResponse)
        fun whenItemSelected(position: Int)
}