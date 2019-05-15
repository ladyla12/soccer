package com.dicoding.soccer.module.team.list

import androidx.lifecycle.ViewModel
import com.dicoding.soccer.db.model.TeamResponse
import com.dicoding.soccer.db.repository.ApiCallback
import com.dicoding.soccer.db.repository.ApiRepository
import com.dicoding.soccer.module.team.TeamInterface
import com.dicoding.soccer.utilities.PermenEspresso

class TeamListViewModel : ViewModel() {
    private var view: TeamInterface? = null
    private lateinit var dataApi: ApiRepository

    fun fragmentCreated(call: TeamInterface, api: ApiRepository) {
        this.view = call
        this.dataApi = api
    }

    fun fragmentDestroyed() {
        this.view = null
    }

    fun loadTeam(leagueId: String) {
        view?.showLoading()
        try {
            PermenEspresso.increase()
            dataApi.getAllTeam(leagueId, object : ApiCallback<TeamResponse?> {
                override fun onLoad(data: TeamResponse?) {
                    PermenEspresso.decrease()
                    view?.hideLoading()
                    data?.let { view?.loadData(it) }
                }

                override fun onError(error: String?) {
                    PermenEspresso.decrease()
                    view?.hideLoading()
                    view?.showMessage("Gagal get data, silahkan coba lagi")
                }
            })
        }
        catch (e: Exception){
            view?.hideLoading()
            view?.showMessage(e.message.toString())
        }
    }
}
