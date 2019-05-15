package com.dicoding.soccer.module.klasemen

import androidx.lifecycle.ViewModel
import com.dicoding.soccer.db.model.LeagueTableResponse
import com.dicoding.soccer.db.repository.ApiCallback
import com.dicoding.soccer.db.repository.ApiRepository
import com.dicoding.soccer.utilities.PermenEspresso

class LeagueKlasementViewModel : ViewModel() {
    private var view: KlasemenInterface? = null
    private lateinit var dataApi: ApiRepository

    fun onFragmentCreated(call: KlasemenInterface, api: ApiRepository){
        this.view = call
        this.dataApi = api
    }

    fun onFragmentDestroyed(){
        this.view = null
    }

    fun loadKlasement(leagueId: String){
        view?.showLoading()
        try {
            PermenEspresso.increase()
            dataApi.getLeagueKlasemen(leagueId, object : ApiCallback<LeagueTableResponse?>{
                override fun onLoad(data: LeagueTableResponse?) {
                    PermenEspresso.decrease()
                    view?.hideLoading()
                    data?.let { view?.loadData(it) }
                }

                override fun onError(error: String?) {
                    PermenEspresso.decrease()
                    view?.hideLoading()
                    view?.showMessage("Please go swipe for refresh data")
                }
            })
        }
        catch (e: Exception){
            view?.hideLoading()
            view?.showMessage(e.message.toString())
        }
    }
}
