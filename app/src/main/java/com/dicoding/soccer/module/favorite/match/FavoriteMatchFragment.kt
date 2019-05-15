package com.dicoding.soccer.module.favorite.match


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dicoding.soccer.R
import com.dicoding.soccer.db.database
import com.dicoding.soccer.module.favorite.FavInterface
import com.dicoding.soccer.module.match.detail.DetailMatchActivity
import com.dicoding.soccer.utilities.FavMatchAdapter
import com.dicoding.soccer.utilities.FavoriteMatch
import com.dicoding.soccer.utilities.invicible
import com.dicoding.soccer.utilities.visible
import kotlinx.android.synthetic.main.fragment_favorite_match.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select

class FavoriteMatchFragment : Fragment(), FavInterface, SwipeRefreshLayout.OnRefreshListener {
    private var favList: MutableList<FavoriteMatch> = mutableListOf()
    private lateinit var favMatchAdapter: FavMatchAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite_match, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        favMatchAdapter = FavMatchAdapter(requireContext(), favList, this) {}
        fav_match_list.layoutManager = LinearLayoutManager(context)
        fav_match_list.adapter = favMatchAdapter
        showFavorite()
        fav_swipe.setOnRefreshListener(this)
    }

    override fun onRefresh() {
        showFavorite()
        fav_swipe.isRefreshing = false
    }

    override fun onResume() {
        super.onResume()
        showFavorite()
    }

    private fun showFavorite() {
        favList.clear()

        requireContext().database.use {
            fav_swipe.isRefreshing = false
            val result = select(FavoriteMatch.MATCH_FAVORITE)
            val fav = result.parseList(classParser<FavoriteMatch>())
            favList.addAll(fav)
            favMatchAdapter.notifyDataSetChanged()
        }
    }

    override fun showLoading() {
        fav_progressbar.visible()
    }

    override fun hideLoading() {
        fav_progressbar.invicible()
    }

    override fun showMessage(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    override fun whenItemSelected(position: Int) {
        val intent = Intent(requireContext(), DetailMatchActivity::class.java)
        intent.putExtra(DetailMatchActivity.eventId, favList[position].matchId)
        intent.putExtra(DetailMatchActivity.team_home_id, favList[position].homeTeamId)
        intent.putExtra(DetailMatchActivity.team_away_id, favList[position].awayTeamId)
        startActivity(intent)
    }
}
