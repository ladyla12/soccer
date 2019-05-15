package com.dicoding.soccer.module.favorite

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.soccer.R
import com.dicoding.soccer.module.favorite.match.FavoriteMatchFragment
import com.dicoding.soccer.module.favorite.team.FavoriteTeamFragment
import com.dicoding.soccer.utilities.TabPagerAdapter
import kotlinx.android.synthetic.main.activity_favorite.*

class FavoriteActivity : AppCompatActivity() {
    private lateinit var tabAdapter: TabPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)
        setSupportActionBar(fav_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Favorite"

        tabAdapter = TabPagerAdapter(supportFragmentManager)
        tabAdapter.addFragments(FavoriteMatchFragment(), "Match")
        tabAdapter.addFragments(FavoriteTeamFragment(), "Team")

        favPager.adapter = tabAdapter
        fav_tab.setupWithViewPager(favPager)

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
