package com.dicoding.soccer.module.search

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.dicoding.soccer.db.RestApiClient
import com.dicoding.soccer.db.model.Match
import com.dicoding.soccer.db.model.SearchResponse
import com.dicoding.soccer.db.repository.ApiCallback
import com.dicoding.soccer.db.repository.ApiRepository
import com.dicoding.soccer.module.match.MatchInterface

class SearchViewModel: ViewModel() {
    private var listMatch: MutableList<Match> = mutableListOf()
    private val dataApi = ApiRepository()
    private var view: MatchInterface? = null

    fun loadMatch(context: Context, teamName: String, call: MatchInterface) {
        Log.d("XXX", "From model : $teamName")
        view?.showLoading()
        try {
            if (RestApiClient.networkCheck(context)){
                view?.showLoading()
                dataApi.searchEvent(teamName, object : ApiCallback<SearchResponse?> {
                    override fun onLoad(data: SearchResponse?) {
                        view?.hideLoading()
                        try {
                            if (data != null && data.event.isNotEmpty()) {
                                listMatch.clear()
                                listMatch.addAll(data.event)
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