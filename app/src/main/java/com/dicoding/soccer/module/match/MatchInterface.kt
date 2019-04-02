package com.dicoding.soccer.module.match

import com.dicoding.soccer.db.model.Match

interface MatchInterface {
        fun showLoading()
        fun hideLoading()
        fun showMessage(msg: String)
        fun loadData(data: List<Match>)
        fun whenItemSelected(position: Int)
}