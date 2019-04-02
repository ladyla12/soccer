package com.dicoding.soccer.module.favorite

interface FavInterface {
    fun showLoading()
    fun hideLoading()
    fun showMessage(msg: String)
    fun whenItemSelected(position: Int)
}