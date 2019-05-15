package com.dicoding.soccer.module.player.list

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dicoding.soccer.R
import com.dicoding.soccer.db.RestApiClient
import com.dicoding.soccer.db.model.Player
import com.dicoding.soccer.db.model.PlayerDetailResponse
import com.dicoding.soccer.db.model.PlayerResponse
import com.dicoding.soccer.db.repository.ApiRepository
import com.dicoding.soccer.module.player.PlayerInterface
import com.dicoding.soccer.module.player.detail.PlayerDetailActivity
import com.dicoding.soccer.utilities.ListPlayerAdapter
import com.dicoding.soccer.utilities.invicible
import com.dicoding.soccer.utilities.visible
import kotlinx.android.synthetic.main.player_list_fragment.*

const val ID_TEAM: String = "TEAM_ID"

class PlayerListFragment : Fragment(), PlayerInterface, SwipeRefreshLayout.OnRefreshListener {

    private var teamId: String? = null
    private var playerList: MutableList<Player> = mutableListOf()
    private lateinit var viewModel: PlayerListViewModel
    private lateinit var playerAdapter: ListPlayerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        teamId = arguments?.getString(ID_TEAM)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.player_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(PlayerListViewModel::class.java)

        playerList.clear()
        viewModel.onFragmentCreated(this, ApiRepository())

        if (RestApiClient.networkCheck(requireContext())) {
            teamId?.let { viewModel.loadPlayers(it) }
        }

        team_player_list.layoutManager = GridLayoutManager(context, 2)
        playerAdapter = ListPlayerAdapter(requireContext(), playerList, this) {}
        team_player_list.adapter = playerAdapter

        frag_player_swipe.setOnRefreshListener(this)
    }

    override fun onRefresh() {
        teamId?.let { viewModel.loadPlayers(it) }
        frag_player_swipe.isRefreshing = false
    }

    override fun showLoading() {
        frag_player_progressbar.visible()
    }

    override fun hideLoading() {
        frag_player_progressbar.invicible()
    }

    override fun showMessage(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }

    override fun loadData(data: PlayerResponse) {
        playerList.clear()
        playerList.addAll(data.player)
        playerAdapter.notifyDataSetChanged()
    }

    override fun loadDetail(data: PlayerDetailResponse) {}

    override fun whenDataSelected(position: Int) {
        val intent = Intent(requireContext(), PlayerDetailActivity::class.java)
        intent.putExtra(PlayerDetailActivity.PLAYER_ID, playerList[position].idPlayer)
        startActivity(intent)
    }

    fun newInstance(teamId: String): PlayerListFragment {
        val fragment = PlayerListFragment()
        val args = Bundle()
        args.putString(ID_TEAM, teamId)
        fragment.arguments = args
        return fragment
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onFragmentDestroyed()
    }
}
