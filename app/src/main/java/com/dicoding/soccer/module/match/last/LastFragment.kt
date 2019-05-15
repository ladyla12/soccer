package com.dicoding.soccer.module.match.last

import android.content.Intent
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
import com.dicoding.soccer.db.model.Match
import com.dicoding.soccer.db.model.MatchResponse
import com.dicoding.soccer.db.repository.ApiRepository
import com.dicoding.soccer.module.match.MatchInterface
import com.dicoding.soccer.module.match.detail.DetailMatchActivity
import com.dicoding.soccer.utilities.ListMatchAdapter
import com.dicoding.soccer.utilities.invicible
import com.dicoding.soccer.utilities.visible
import kotlinx.android.synthetic.main.last_fragment.*

const val ID_LEAGUE = "id_league"

class LastFragment : Fragment(), MatchInterface, SwipeRefreshLayout.OnRefreshListener {
    private var mIdLeague: String? = null

    private var matchList: MutableList<Match> = mutableListOf()
    private lateinit var viewModel: LastViewModel
    private lateinit var listMatchAdapter: ListMatchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mIdLeague = arguments?.getString(ID_LEAGUE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.last_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(LastViewModel::class.java)

        matchList.clear()
        viewModel.onFragmentCreated(this, ApiRepository())
        if (RestApiClient.networkCheck(requireContext())){
            mIdLeague?.let { viewModel.loadMatch(it) }
        }

        last_match_list.layoutManager = LinearLayoutManager(context)
        listMatchAdapter = ListMatchAdapter(this.requireContext(), matchList, this) {}
        last_match_list.adapter = listMatchAdapter

        swipeId.setOnRefreshListener(this)

    }

    override fun whenItemSelected(position: Int) {
        val intent = Intent(context, DetailMatchActivity::class.java)
        intent.putExtra(DetailMatchActivity.eventId, matchList[position].eventId)
        intent.putExtra(DetailMatchActivity.team_home_id, matchList[position].homeTeamId)
        intent.putExtra(DetailMatchActivity.team_away_id, matchList[position].awayTeamId)
        startActivity(intent)
    }

    override fun onRefresh() {
        mIdLeague?.let { viewModel.loadMatch(it) }
        swipeId.isRefreshing = false
    }

    override fun showLoading() {
        progressBar.visible()
    }

    override fun hideLoading() {
        progressBar.invicible()
    }

    override fun showMessage(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    override fun loadData(data: MatchResponse) {
        matchList.addAll(data.events)
        listMatchAdapter.notifyDataSetChanged()
    }

    fun newInstance(idLeague: String): LastFragment {
        val fragment = LastFragment()
        val args = Bundle()
        args.putString(ID_LEAGUE, idLeague)
        fragment.arguments = args
        return fragment
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onFragmentDestroyed()
    }

}
