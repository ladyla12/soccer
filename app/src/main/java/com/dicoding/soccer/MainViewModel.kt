package com.dicoding.soccer

import androidx.lifecycle.ViewModel
import com.dicoding.soccer.db.model.LeagueResponse
import com.dicoding.soccer.db.repository.ApiCallback
import com.dicoding.soccer.db.repository.ApiRepository
import com.dicoding.soccer.utilities.PermenEspresso

class MainViewModel : ViewModel() {
    private var view: MainInterface? = null
    private lateinit var dataApi: ApiRepository

    fun activityCreated(call: MainInterface, api: ApiRepository) {
        this.view = call
        this.dataApi = api
    }

    fun activityDestroyed() {
        this.view = null
    }

    fun loadLeague(leagueId: String) {
        view?.showLoading()
        try {
            PermenEspresso.increase()
            dataApi.getLeague(leagueId, object : ApiCallback<LeagueResponse?> {
                override fun onLoad(data: LeagueResponse?) {
                    PermenEspresso.decrease()
                    view?.hideLoading()
                    data?.let { view?.loadLeague(it) }
                }

                override fun onError(error: String?) {
                    PermenEspresso.decrease()
                    view?.hideLoading()
                    view?.showMessage("Please go swipe for refresh data")
                }
            })
        } catch (e: Exception) {
            view?.hideLoading()
            view?.showMessage(e.toString())
        }
    }
}