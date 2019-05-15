package com.dicoding.soccer

import androidx.test.espresso.IdlingRegistry
import com.dicoding.soccer.db.model.LeagueResponse
import com.dicoding.soccer.db.repository.ApiCallback
import com.dicoding.soccer.db.repository.ApiRepository
import com.dicoding.soccer.utilities.PermenEspresso
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.verify
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class MainViewModelTest {
    @Mock private lateinit var apiRepository: ApiRepository
    @Mock private lateinit var mainInterface: MainInterface
    @Mock private lateinit var response: LeagueResponse

    private var model = MainViewModel()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        model.activityCreated(mainInterface, apiRepository)
        IdlingRegistry.getInstance().register(PermenEspresso.idlingResource)
    }

    @Test
    fun loadLeague() {
        val id = "4335"
        model.loadLeague(id)

        argumentCaptor<ApiCallback<LeagueResponse?>>().apply {
            verify(apiRepository).getLeague(eq(id), capture())
            firstValue.onLoad(response)
        }

        Mockito.verify(mainInterface).showLoading()
        Mockito.verify(mainInterface).hideLoading()
        Mockito.verify(mainInterface).loadLeague(response)
    }

    @Test
    fun loadLeagueError() {
        model.loadLeague("")

        argumentCaptor<ApiCallback<LeagueResponse?>>().apply {
            verify(apiRepository).getLeague(eq(""), capture())
            firstValue.onError("No Data Found!")
        }
        Mockito.verify(mainInterface).showLoading()
        Mockito.verify(mainInterface).showMessage("Please go swipe for refresh data")
        Mockito.verify(mainInterface).hideLoading()
    }
}