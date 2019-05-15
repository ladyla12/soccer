package com.dicoding.soccer.module.match.detail

import androidx.lifecycle.ViewModel
import com.dicoding.soccer.db.model.MatchDetailResponse
import com.dicoding.soccer.db.model.TeamResponse
import com.dicoding.soccer.db.repository.ApiCallback
import com.dicoding.soccer.db.repository.ApiRepository
import com.dicoding.soccer.module.match.DetailMatchInterface
import com.dicoding.soccer.utilities.PermenEspresso

class DetailViewModel: ViewModel() {
    private var view: DetailMatchInterface? = null
    private lateinit var dataApi: ApiRepository

    fun activityCreated(call: DetailMatchInterface, api: ApiRepository) {
        this.view = call
        this.dataApi = api
    }

    fun activityDestroyed() {
        this.view = null
    }

    fun loadMatch(eventId: String) {
        view?.showLoading()
        try {
            PermenEspresso.increase()
            dataApi.getDetailMatch(eventId, object : ApiCallback<MatchDetailResponse?>{
                override fun onLoad(data: MatchDetailResponse?) {
                    PermenEspresso.decrease()
                    view?.hideLoading()
                    if (data?.events.isNullOrEmpty()){
                        view?.showMessage("Match Event Not Found")
                    }
                    else {
                        data?.let { view?.loadData(it) }
                    }
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

    fun loadTeam(idTeam: String) {
        view?.showLoading()
        try {
            PermenEspresso.increase()
            dataApi.getDetailTeam(idTeam, object : ApiCallback<TeamResponse?>{
                override fun onLoad(data: TeamResponse?) {
                    PermenEspresso.decrease()
                    view?.hideLoading()
                    data?.let { view?.loadImage(it, idTeam) }
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