package com.dicoding.soccer.module.match.last

import android.content.Context
import androidx.lifecycle.ViewModel
import com.dicoding.soccer.db.RestApiClient
import com.dicoding.soccer.db.model.Match
import com.dicoding.soccer.db.model.MatchResponse
import com.dicoding.soccer.db.repository.ApiCallback
import com.dicoding.soccer.db.repository.ApiRepository
import com.dicoding.soccer.module.match.MatchInterface

class LastViewModel : ViewModel() {
    private var listMatch: MutableList<Match> = mutableListOf()
    private val dataApi = ApiRepository()
    private var view: MatchInterface? = null

    fun loadMatch(context: Context, leagueId: String, call: MatchInterface) {
        view?.showLoading()
        try {
            if (RestApiClient.networkCheck(context)){
                view?.showLoading()
                dataApi.getPastEvent(leagueId, object : ApiCallback<MatchResponse?> {
                    override fun onLoad(data: MatchResponse?) {
                        view?.hideLoading()
                        try {
                            if (data != null && data.events.isNotEmpty()) {
                                listMatch.clear()
                                listMatch.addAll(data.events)
                                view = call
                                view!!.loadData(listMatch)
                            }
                            else {
                                view?.showMessage("No Data Found!")
                            }
                        } catch (e: Exception) {
                            view?.showMessage("Please go swipe for refresh data")
                        }
                    }

                    override fun onError(error: String?) {
                        view?.showMessage(error.toString())
                    }

                })
            }
            else{
                view?.hideLoading()
                view?.showMessage("Please check your connection")
            }
        }
        catch (e: Exception){
            view?.hideLoading()
            view?.showMessage(e.message.toString())
        }
    }
}
