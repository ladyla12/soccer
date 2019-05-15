package com.dicoding.soccer.module.player.detail

import androidx.lifecycle.ViewModel
import com.dicoding.soccer.db.model.PlayerDetailResponse
import com.dicoding.soccer.db.repository.ApiCallback
import com.dicoding.soccer.db.repository.ApiRepository
import com.dicoding.soccer.module.player.PlayerInterface
import com.dicoding.soccer.utilities.PermenEspresso

class PlayerDetailViewModel: ViewModel() {
    private var view: PlayerInterface? = null
    private lateinit var dataApi: ApiRepository

    fun onActivityCreated(call: PlayerInterface, api: ApiRepository){
        this.view = call
        this.dataApi = api
    }

    fun onActivityDestroyed(){
        this.view = null
    }

    fun loadPlayers(playerId: String){
        view?.showLoading()
        try {
            PermenEspresso.increase()
            dataApi.getDetailPlayer(playerId, object : ApiCallback<PlayerDetailResponse?> {
                override fun onLoad(data: PlayerDetailResponse?) {
                    PermenEspresso.decrease()
                    view?.hideLoading()

                    if (data?.players.isNullOrEmpty()){
                        view?.showMessage("Player Information Not Found")
                    }
                    else {
                        data?.let { view?.loadDetail(it) }
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
}