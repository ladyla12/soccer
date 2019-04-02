package com.dicoding.soccer.module.match.detail

import android.content.Context
import androidx.lifecycle.ViewModel
import com.dicoding.soccer.db.RestApiClient
import com.dicoding.soccer.db.model.MatchDetail
import com.dicoding.soccer.db.model.MatchDetailResponse
import com.dicoding.soccer.db.model.Team
import com.dicoding.soccer.db.model.TeamResponse
import com.dicoding.soccer.db.repository.ApiCallback
import com.dicoding.soccer.db.repository.ApiRepository
import com.dicoding.soccer.module.match.DetailMatchInterface

class DetailViewModel: ViewModel() {
    private var listMatch: MutableList<MatchDetail> = mutableListOf()
    private var listTeam: MutableList<Team> = mutableListOf()
    private val dataApi = ApiRepository()
    private var view: DetailMatchInterface? = null

    fun loadMatch(context: Context, eventId: String, call: DetailMatchInterface) {
        view?.showLoading()
        try {
            if (RestApiClient.networkCheck(context)){
                view?.showLoading()
                dataApi.getDetailMatch(eventId, object : ApiCallback<MatchDetailResponse?> {
                    override fun onLoad(data: MatchDetailResponse?) {
                        view?.hideLoading()
                        try {
                            if (data != null && data.events.isNotEmpty()) {
                                listMatch.clear()
                                listMatch.addAll(data.events)
                                view = call
                                view?.loadData(listMatch)
                                loadTeam(context, data.events[0].homeTeamId.toString())
                                loadTeam(context, data.events[0].awayTeamId.toString())
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

    fun loadTeam(context: Context, idTeam: String) {
        view?.showLoading()
        try {
            if (RestApiClient.networkCheck(context)){
                view?.showLoading()
                dataApi.getDetailTeam(idTeam, object : ApiCallback<TeamResponse?> {
                    override fun onLoad(data: TeamResponse?) {
                        view?.hideLoading()
                        try {
                            if (data != null && data.teams.isNotEmpty()) {
                                listTeam.clear()
                                listTeam.addAll(data.teams)
                                view?.loadImage(listTeam, idTeam)
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