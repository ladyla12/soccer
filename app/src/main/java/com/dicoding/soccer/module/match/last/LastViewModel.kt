package com.dicoding.soccer.module.match.last

import androidx.lifecycle.ViewModel
import com.dicoding.soccer.db.model.MatchResponse
import com.dicoding.soccer.db.repository.ApiCallback
import com.dicoding.soccer.db.repository.ApiRepository
import com.dicoding.soccer.module.match.MatchInterface
import com.dicoding.soccer.utilities.PermenEspresso

class LastViewModel : ViewModel() {
    private var view: MatchInterface? = null
    private lateinit var dataApi: ApiRepository

    fun onFragmentCreated(call: MatchInterface, api: ApiRepository){
        this.view = call
        this.dataApi = api
    }

    fun onFragmentDestroyed(){
        this.view = null
    }

    fun loadMatch(leagueId: String) {
        view?.showLoading()
        try {
            PermenEspresso.increase()
            dataApi.getPastEvent(leagueId, object : ApiCallback<MatchResponse?>{
                override fun onLoad(data: MatchResponse?) {
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
