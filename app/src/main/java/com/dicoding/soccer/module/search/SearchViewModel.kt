package com.dicoding.soccer.module.search

import androidx.lifecycle.ViewModel
import com.dicoding.soccer.db.model.MatchSearchResponse
import com.dicoding.soccer.db.model.TeamResponse
import com.dicoding.soccer.db.repository.ApiCallback
import com.dicoding.soccer.db.repository.ApiRepository
import com.dicoding.soccer.utilities.PermenEspresso

class SearchViewModel : ViewModel() {
    private var view: SearchInterface? = null
    private lateinit var dataApi: ApiRepository

    fun activityCreated(call: SearchInterface, api: ApiRepository) {
        this.view = call
        this.dataApi = api
    }

    fun activityDestroyed() {
        this.view = null
    }

    fun loadMatch(teamName: String) {
        view?.showLoading()
        try {
            PermenEspresso.increase()
            dataApi.searchEvent(teamName, object : ApiCallback<MatchSearchResponse?> {
                override fun onLoad(data: MatchSearchResponse?) {
                    PermenEspresso.decrease()
                    view?.hideLoading()

                    if (data?.event.isNullOrEmpty()){
                        view?.showMessage("$teamName Not Found")
                    }
                    else {
                        data?.let { view?.searchMatchResult(it) }
                    }
                }

                override fun onError(error: String?) {
                    PermenEspresso.decrease()
                    view?.hideLoading()
                    view?.showMessage("Please go swipe for refresh data")
                }
            })
        } catch (e: Exception) {
            PermenEspresso.decrease()
            view?.hideLoading()
            view?.showMessage(e.message.toString())
        }
    }

    fun loadTeam(teamName: String){
        view?.showLoading()
        try {
            PermenEspresso.increase()
            dataApi.searchTeam(teamName, object : ApiCallback<TeamResponse?>{
                override fun onLoad(data: TeamResponse?) {
                    PermenEspresso.decrease()
                    view?.hideLoading()

                    if (data?.teams.isNullOrEmpty()){
                        view?.showMessage("$teamName Not Found")
                    }
                    else {
                        data?.let { view?.searchTeamResult(it) }
                    }
                }

                override fun onError(error: String?) {
                    PermenEspresso.decrease()
                    view?.hideLoading()
                    view?.showMessage("Please go swipe for refresh data")
                }
            })
        }
        catch (e: java.lang.Exception) {
            PermenEspresso.decrease()
            view?.hideLoading()
            view?.showMessage(e.message.toString())
        }
    }
}