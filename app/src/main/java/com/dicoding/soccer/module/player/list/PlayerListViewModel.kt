package com.dicoding.soccer.module.player.list

import androidx.lifecycle.ViewModel
import com.dicoding.soccer.db.model.PlayerResponse
import com.dicoding.soccer.db.repository.ApiCallback
import com.dicoding.soccer.db.repository.ApiRepository
import com.dicoding.soccer.module.player.PlayerInterface
import com.dicoding.soccer.utilities.PermenEspresso

class PlayerListViewModel : ViewModel() {
    private var view: PlayerInterface? = null
    private lateinit var dataApi: ApiRepository

    fun onFragmentCreated(call: PlayerInterface, api: ApiRepository){
        this.view = call
        this.dataApi = api
    }

    fun onFragmentDestroyed(){
        this.view = null
    }

    fun loadPlayers(teamId: String){
        view?.showLoading()
        try {
            PermenEspresso.increase()
            dataApi.getAllPlayer(teamId, object : ApiCallback<PlayerResponse?>{
                override fun onLoad(data: PlayerResponse?) {
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
