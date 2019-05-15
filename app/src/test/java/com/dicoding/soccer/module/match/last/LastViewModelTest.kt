package com.dicoding.soccer.module.match.last

import androidx.test.espresso.IdlingRegistry
import com.dicoding.soccer.db.model.MatchResponse
import com.dicoding.soccer.db.repository.ApiCallback
import com.dicoding.soccer.db.repository.ApiRepository
import com.dicoding.soccer.module.match.MatchInterface
import com.dicoding.soccer.utilities.PermenEspresso
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.verify
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class LastViewModelTest {
    @Mock private lateinit var apiRepository: ApiRepository
    @Mock private lateinit var matchInterface: MatchInterface
    @Mock private lateinit var response: MatchResponse

    private var model = LastViewModel()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        model.onFragmentCreated(matchInterface, apiRepository)
        IdlingRegistry.getInstance().register(PermenEspresso.idlingResource)
    }

    @Test
    fun loadMatch() {
        val id = "4335"
        model.loadMatch(id)

        argumentCaptor<ApiCallback<MatchResponse?>>().apply {
            verify(apiRepository).getPastEvent(eq(id), capture())
            firstValue.onLoad(response)
        }

        Mockito.verify(matchInterface).showLoading()
        Mockito.verify(matchInterface).hideLoading()
        Mockito.verify(matchInterface).loadData(response)
    }

    @Test
    fun loadMatchError(){
        model.loadMatch("")

        argumentCaptor<ApiCallback<MatchResponse?>>().apply {
            verify(apiRepository).getPastEvent(eq(""), capture())
            firstValue.onError("No Data Found!")
        }

        Mockito.verify(matchInterface).showLoading()
        Mockito.verify(matchInterface).showMessage("Please go swipe for refresh data")
        Mockito.verify(matchInterface).hideLoading()
    }
}