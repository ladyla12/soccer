package com.dicoding.soccer.module.search

import androidx.test.espresso.IdlingRegistry
import com.dicoding.soccer.db.model.MatchSearchResponse
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

class SearchViewModelTest {
    @Mock private lateinit var apiRepository: ApiRepository
    @Mock private lateinit var matchInterface: SearchInterface
    @Mock private lateinit var responseMatch: MatchSearchResponse

    private var model = SearchViewModel()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        model.activityCreated(matchInterface, apiRepository)
        IdlingRegistry.getInstance().register(PermenEspresso.idlingResource)
    }

    @Test
    fun loadMatch() {
        val teamName = "Juventus"

        model.loadMatch(teamName)

        argumentCaptor<ApiCallback<MatchSearchResponse?>>().apply {
            verify(apiRepository).searchEvent(eq(teamName), capture())
            firstValue.onLoad(responseMatch)
        }

        Mockito.verify(matchInterface).showLoading()
        Mockito.verify(matchInterface).hideLoading()
        Mockito.verify(matchInterface).showMessage("$teamName Not Found")
        Mockito.verify(matchInterface).searchMatchResult(responseMatch)
    }

    @Test
    fun loadMatchError() {
        model.loadMatch("")

        argumentCaptor<ApiCallback<MatchSearchResponse?>>().apply {
            verify(apiRepository).searchEvent(eq(""), capture())
            firstValue.onError("No Data Found!")
        }

        Mockito.verify(matchInterface).showLoading()
        Mockito.verify(matchInterface).showMessage("Please go swipe for refresh data")
        Mockito.verify(matchInterface).hideLoading()
    }
}