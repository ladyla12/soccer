package com.dicoding.soccer.module.team.list

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
import com.dicoding.soccer.db.model.Team
import com.dicoding.soccer.db.model.TeamResponse
import com.dicoding.soccer.db.repository.ApiRepository
import com.dicoding.soccer.module.team.TeamActivity
import com.dicoding.soccer.module.team.TeamInterface
import com.dicoding.soccer.utilities.ListTeamAdapter
import com.dicoding.soccer.utilities.invicible
import com.dicoding.soccer.utilities.visible
import kotlinx.android.synthetic.main.team_list_fragment.*

const val ID_LEAGUE = "id_league"

class TeamListFragment : Fragment(), TeamInterface, SwipeRefreshLayout.OnRefreshListener {

    private var idLeague: String? = null
    private var teamList: MutableList<Team> = mutableListOf()
    private lateinit var viewModel: TeamListViewModel
    private lateinit var teamAdapter: ListTeamAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        idLeague = arguments?.getString(ID_LEAGUE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.team_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(TeamListViewModel::class.java)

        teamList.clear()
        viewModel.fragmentCreated(this, ApiRepository())
        if (RestApiClient.networkCheck(requireContext())){
            idLeague?.let { viewModel.loadTeam(it) }
        }

        league_team_list.layoutManager = LinearLayoutManager(context)
        teamAdapter = ListTeamAdapter(requireContext(), teamList, this){}
        league_team_list.adapter = teamAdapter

        frag_team_swipe.setOnRefreshListener(this)
    }

    override fun onRefresh() {
        idLeague?.let { viewModel.loadTeam(it) }
        frag_team_swipe.isRefreshing = false
    }

    override fun showLoading() {
        frag_team_progressbar.visible()
    }

    override fun hideLoading() {
        frag_team_progressbar.invicible()
    }

    override fun showMessage(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    override fun loadData(data: TeamResponse) {
        teamList.clear()
        teamList.addAll(data.teams)
        teamAdapter.notifyDataSetChanged()
    }

    override fun whenItemSelected(position: Int) {
        val intent = Intent(context, TeamActivity::class.java)
        intent.putExtra(TeamActivity.teamId, teamList[position].teamId)
        intent.putExtra(TeamActivity.teamName, teamList[position].teamName)
        intent.putExtra(TeamActivity.teamBadge, teamList[position].teamBadge)
        startActivity(intent)
    }

    fun newInstance(leagueId: String): TeamListFragment {
        val fragment = TeamListFragment()
        val args = Bundle()
        args.putString(ID_LEAGUE, leagueId)
        fragment.arguments = args
        return fragment
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.fragmentDestroyed()
    }
}
