package com.dicoding.soccer.module.match.detail

import androidx.test.espresso.IdlingRegistry
import com.dicoding.soccer.db.model.MatchDetailResponse
import com.dicoding.soccer.db.model.TeamResponse
import com.dicoding.soccer.db.repository.ApiCallback
import com.dicoding.soccer.db.repository.ApiRepository
import com.dicoding.soccer.module.match.DetailMatchInterface
import com.dicoding.soccer.utilities.PermenEspresso
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.verify
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class DetailViewModelTest {
    @Mock private lateinit var apiRespository: ApiRepository
    @Mock private lateinit var detailMatchInterface: DetailMatchInterface
    @Mock private lateinit var matchResponse: MatchDetailResponse
    @Mock private lateinit var teamResponse: TeamResponse

    private var model = DetailViewModel()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        model.activityCreated(detailMatchInterface, apiRespository)
        IdlingRegistry.getInstance().register(PermenEspresso.idlingResource)
    }

    @Test
    fun loadMatch() {
        val matchId = "576789"

        model.loadMatch(matchId)

        argumentCaptor<ApiCallback<MatchDetailResponse?>>().apply {
            verify(apiRespository).getDetailMatch(eq(matchId), capture())
            firstValue.onLoad(matchResponse)
        }

        Mockito.verify(detailMatchInterface).showLoading()
        Mockito.verify(detailMatchInterface).hideLoading()
        Mockito.verify(detailMatchInterface).loadData(matchResponse)
    }

    @Test
    fun loadTeam() {
        val teamId = "133604"

        model.loadTeam(teamId)

        argumentCaptor<ApiCallback<TeamResponse?>>().apply {
            verify(apiRespository).getDetailTeam(eq(teamId), capture())
            firstValue.onLoad(teamResponse)
        }

        Mockito.verify(detailMatchInterface).showLoading()
        Mockito.verify(detailMatchInterface).hideLoading()
        Mockito.verify(detailMatchInterface).loadImage(teamResponse, teamId)
    }

    @Test
    fun loadMatchError() {
        model.loadMatch("")

        argumentCaptor<ApiCallback<MatchDetailResponse?>>().apply {
            verify(apiRespository).getDetailMatch(eq(""), capture())
            firstValue.onError("No Data Found!")
        }

        Mockito.verify(detailMatchInterface).showLoading()
        Mockito.verify(detailMatchInterface).showMessage("Gagal get data, silahkan coba lagi")
        Mockito.verify(detailMatchInterface).hideLoading()
    }

    @Test
    fun loadTeamError() {
        model.loadTeam("")

        argumentCaptor<ApiCallback<TeamResponse?>>().apply {
            verify(apiRespository).getDetailTeam(eq(""), capture())
            firstValue.onError("No Data Found!")
        }

        Mockito.verify(detailMatchInterface).showLoading()
        Mockito.verify(detailMatchInterface).showMessage("Gagal get data, silahkan coba lagi")
        Mockito.verify(detailMatchInterface).hideLoading()
    }
}