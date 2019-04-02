package com.dicoding.soccer

import android.content.Context
import com.dicoding.soccer.db.model.LeagueResponse
import com.dicoding.soccer.db.repository.ApiRepository
import androidx.test.espresso.*
import com.dicoding.soccer.db.repository.ApiCallback
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
    @Mock
    private lateinit var apiRepository: ApiRepository

    @Mock
    private lateinit var mainInterface: MainInterface

    @Mock
    private lateinit var response: LeagueResponse

    private var model: MainViewModel? = null
    private lateinit var context: Context

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        IdlingRegistry.getInstance().register(PermenEspresso.idlingResource)
    }

    @Test
    fun loadLeague() {
        val id = "4335"
        model?.loadLeague(context, id, mainInterface)
        argumentCaptor<ApiCallback<LeagueResponse?>>().apply {
            verify(apiRepository).getLeague(eq(id), capture())
            firstValue.onLoad(response)
        }

        Mockito.verify(mainInterface).showLoading()
        Mockito.verify(mainInterface).hideLoading()
        Mockito.verify(mainInterface).loadLeague(response)
    }
}