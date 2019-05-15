package com.dicoding.soccer.module.klasemen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dicoding.soccer.R
import com.dicoding.soccer.db.RestApiClient
import com.dicoding.soccer.db.model.LeagueTable
import com.dicoding.soccer.db.model.LeagueTableResponse
import com.dicoding.soccer.db.repository.ApiRepository
import com.dicoding.soccer.utilities.ListKlasemenAdapter
import com.dicoding.soccer.utilities.invicible
import com.dicoding.soccer.utilities.visible
import kotlinx.android.synthetic.main.league_klasement_fragment.*

const val ID_LEAGUE = "id_league"

class LeagueKlasement : Fragment(), KlasemenInterface, SwipeRefreshLayout.OnRefreshListener {

    private var idLeague: String? = null
    private var klasement: MutableList<LeagueTable> = mutableListOf()
    private lateinit var viewModel: LeagueKlasementViewModel
    private lateinit var listKlasement: ListKlasemenAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        idLeague = arguments?.getString(ID_LEAGUE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.league_klasement_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(LeagueKlasementViewModel::class.java)

        klasement.clear()
        viewModel.onFragmentCreated(this, ApiRepository())
        if (RestApiClient.networkCheck(requireContext())){
            idLeague?.let { viewModel.loadKlasement(it) }
        }

        klasement_list.layoutManager = LinearLayoutManager(context)
        listKlasement = ListKlasemenAdapter(requireContext(), klasement)
        klasement_list.adapter = listKlasement

        klasement_swipe.setOnRefreshListener(this)
    }

    override fun showLoading() {
        klasement_progressbar.visible()
    }

    override fun hideLoading() {
        klasement_progressbar.invicible()
    }

    override fun showMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun loadData(data: LeagueTableResponse) {
        klasement.clear()
        klasement.addAll(data.table)
        listKlasement.notifyDataSetChanged()
    }

    override fun onRefresh() {
        idLeague?.let { viewModel.loadKlasement(it) }
        klasement_swipe.isRefreshing = false
    }

    fun newIntance(leagueId: String): LeagueKlasement{
        val fragment = LeagueKlasement()
        val args = Bundle()
        args.putString(ID_LEAGUE, leagueId)
        fragment.arguments = args
        return fragment
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onFragmentDestroyed()
    }
}
