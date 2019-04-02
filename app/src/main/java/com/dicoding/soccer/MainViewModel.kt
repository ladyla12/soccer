package com.dicoding.soccer

import android.content.Context
import androidx.lifecycle.ViewModel
import com.dicoding.soccer.db.RestApiClient
import com.dicoding.soccer.db.model.LeagueResponse
import com.dicoding.soccer.db.repository.ApiCallback
import com.dicoding.soccer.db.repository.ApiRepository
import com.dicoding.soccer.utilities.PermenEspresso

class MainViewModel : ViewModel() {
    private var dataApi = ApiRepository()
    private var view: MainInterface? = null

    fun loadLeague(context: Context, leagueId: String, call: MainInterface) {
        view?.showLoading()
        view = call
        try {
            if (RestApiClient.networkCheck(context)) {
                PermenEspresso.increase()
                view?.showLoading()
                dataApi.getLeague(leagueId, object : ApiCallback<LeagueResponse?> {
                    override fun onLoad(data: LeagueResponse?) {
                        PermenEspresso.decrease()
                        view?.hideLoading()
                        if (data != null && data.leagues.isNotEmpty()) {
                            view?.loadLeague(data)
                        } else {
                            view?.showMessage("No Data Found!")
                        }
                    }

                    override fun onError(error: String?) {
                        PermenEspresso.decrease()
                        view?.showMessage("Please go swipe for refresh data$error")
                    }
                })
            }
            else {
                view?.hideLoading()
                view?.showMessage("Please check your connection")
            }
        } catch (e: Exception) {
            view?.hideLoading()
            view?.showMessage(e.toString())
        }
    }
}