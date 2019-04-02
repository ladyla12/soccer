package com.dicoding.soccer.module.favorite

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dicoding.soccer.R
import com.dicoding.soccer.db.database
import com.dicoding.soccer.module.match.detail.DetailMatchActivity
import com.dicoding.soccer.utilities.FavMatchAdapter
import com.dicoding.soccer.utilities.Favorite
import com.dicoding.soccer.utilities.invicible
import com.dicoding.soccer.utilities.visible
import kotlinx.android.synthetic.main.activity_favorite.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select

class FavoriteActivity : AppCompatActivity(), FavInterface, SwipeRefreshLayout.OnRefreshListener {
    private var favList: MutableList<Favorite> = mutableListOf()
    private lateinit var favMatchAdapter: FavMatchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)
        setSupportActionBar(fav_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        val titleSupport = supportActionBar
        titleSupport?.title = "Favorite Match"

        favMatchAdapter = FavMatchAdapter(applicationContext, favList, this){}
        fav_match_list.layoutManager = LinearLayoutManager(this)
        fav_match_list.adapter = favMatchAdapter
        showFavorite()
    }

    override fun onResume() {
        super.onResume()
        showFavorite()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun showFavorite(){
        favList.clear()
        this.database.use {
            fav_swipe.isRefreshing = false
            val result = select(Favorite.TABLE_FAVORITE)
            val fav = result.parseList(classParser<Favorite>())
            favList.addAll(fav)
            favMatchAdapter.notifyDataSetChanged()
        }
    }

    override fun onRefresh() {
        fav_swipe.isRefreshing = false
        showFavorite()
    }

    override fun showLoading() {
        fav_progressbar.visible()
    }

    override fun hideLoading() {
        fav_progressbar.invicible()
    }

    override fun showMessage(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    override fun whenItemSelected(position: Int) {
        val intent = Intent(this, DetailMatchActivity::class.java)
        intent.putExtra(DetailMatchActivity.eventId, favList[position].matchId)
        intent.putExtra(DetailMatchActivity.team_home_id, favList[position].homeTeamId)
        intent.putExtra(DetailMatchActivity.team_away_id, favList[position].awayTeamId)
        startActivity(intent)
    }
}
