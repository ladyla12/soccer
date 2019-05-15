package com.dicoding.soccer.module.favorite.team


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dicoding.soccer.R
import com.dicoding.soccer.db.database
import com.dicoding.soccer.module.favorite.FavInterface
import com.dicoding.soccer.module.team.TeamActivity
import com.dicoding.soccer.utilities.FavTeamAdapter
import com.dicoding.soccer.utilities.FavoriteTeam
import com.dicoding.soccer.utilities.invicible
import com.dicoding.soccer.utilities.visible
import kotlinx.android.synthetic.main.fragment_favorite_team.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select

class FavoriteTeamFragment : Fragment(), FavInterface, SwipeRefreshLayout.OnRefreshListener {
    private var favTeamList: MutableList<FavoriteTeam> = mutableListOf()
    private lateinit var favTeamAdapter: FavTeamAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite_team, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        favTeamAdapter = FavTeamAdapter(requireContext(), favTeamList, this) {}
        fav_team_list.layoutManager = GridLayoutManager(requireContext(), 2)
        fav_team_list.adapter = favTeamAdapter

        showFavorite()

        fav_team_swipe.setOnRefreshListener(this)

    }

    private fun showFavorite() {
        favTeamList.clear()

        requireContext().database.use {
            fav_team_swipe.isRefreshing = false
            val result = select(FavoriteTeam.TEAM_FAVORITE)
            val fav = result.parseList(classParser<FavoriteTeam>())

            favTeamList.addAll(fav)
            favTeamAdapter.notifyDataSetChanged()
        }
    }

    override fun onResume() {
        super.onResume()
        showFavorite()
    }

    override fun onRefresh() {
        showFavorite()
        fav_team_swipe.isRefreshing = false
    }

    override fun showLoading() {
        fav_team_progressbar.visible()
    }

    override fun hideLoading() {
        fav_team_progressbar.invicible()
    }

    override fun showMessage(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
    }

    override fun whenItemSelected(position: Int) {
        val intent = Intent(requireContext(), TeamActivity::class.java)
        intent.putExtra(TeamActivity.teamId, favTeamList[position].teamId)
        intent.putExtra(TeamActivity.teamName, favTeamList[position].teamName)
        intent.putExtra(TeamActivity.teamBadge, favTeamList[position].teamBadge)
        startActivity(intent)
    }
}
