package com.dicoding.soccer

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.dicoding.soccer.db.RestApiClient
import com.dicoding.soccer.db.model.League
import com.dicoding.soccer.db.model.LeagueResponse
import com.dicoding.soccer.db.repository.ApiRepository
import com.dicoding.soccer.module.favorite.FavoriteActivity
import com.dicoding.soccer.module.klasemen.LeagueKlasement
import com.dicoding.soccer.module.match.last.LastFragment
import com.dicoding.soccer.module.match.next.NextFragment
import com.dicoding.soccer.module.search.SearchActivity
import com.dicoding.soccer.module.team.list.TeamListFragment
import com.dicoding.soccer.utilities.LeaguePagerAdapter
import com.dicoding.soccer.utilities.TabPagerAdapter
import com.dicoding.soccer.utilities.invicible
import com.dicoding.soccer.utilities.visible
import com.google.android.gms.security.ProviderInstaller
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MainInterface {
    private var leagueItem: MutableList<League> = mutableListOf()
    private lateinit var adapter: LeaguePagerAdapter
    private lateinit var tabAdapter: TabPagerAdapter
    private lateinit var mvModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        try {
            ProviderInstaller.installIfNeeded(applicationContext)
        } catch (e: Exception) {
            showMessage("${e.message}")
        }

        setSupportActionBar(main_toolbar)
        val titleSupport = supportActionBar
        titleSupport?.title = "League Match"
        mvModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        val id = resources.getStringArray(R.array.league_id)

        leagueItem.clear()
        mvModel.activityCreated(this, ApiRepository())
        if (RestApiClient.networkCheck(applicationContext)) {
            id.forEachIndexed { _, element ->
                element.let { it1 -> mvModel.loadLeague(it1) }
            }
        }


        adapter = LeaguePagerAdapter(this, leagueItem)
        leaguePager.adapter = adapter
        leaguePager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {

                val matchAdapter = TabPagerAdapter(supportFragmentManager)

                matchAdapter.addFragments(
                    LastFragment().newInstance(leagueItem[position].leagueId.toString()),
                    "Last Match"
                )

                matchAdapter.addFragments(
                    NextFragment().newInstance(leagueItem[position].leagueId.toString()),
                    "Upcoming Match"
                )

                matchAdapter.addFragments(
                    LeagueKlasement().newIntance(leagueItem[position].leagueId.toString()),
                    "Klasemen"
                )

                matchAdapter.addFragments(
                    TeamListFragment().newInstance(leagueItem[position].leagueId.toString()),
                    "Teams"
                )

                matchPager.adapter = matchAdapter
                leagueTabMatch.setupWithViewPager(matchPager)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val src = menu?.findItem(R.id.app_bar_search)?.actionView as SearchView
        src.queryHint = getString(R.string.team_search)
        src.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val intent = Intent(applicationContext, SearchActivity::class.java)
                intent.putExtra(SearchActivity.QUERY, query)
                intent.putExtra(SearchActivity.REQUEST_CODE, leagueTabMatch.selectedTabPosition)
                startActivity(intent)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.app_bar_favorite -> {
                val intent = Intent(this, FavoriteActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showMessage(string: String) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show()
    }

    override fun showLoading() {
        main_pg.visible()
    }

    override fun hideLoading() {
        main_pg.invicible()
    }

    override fun loadLeague(data: LeagueResponse) {
        leagueItem.addAll(data.leagues)
        adapter.notifyDataSetChanged()

        tabAdapter = TabPagerAdapter(supportFragmentManager)
        tabAdapter.addFragments(LastFragment().newInstance(leagueItem[0].leagueId.toString()), "Last Match")
        tabAdapter.addFragments(NextFragment().newInstance(leagueItem[0].leagueId.toString()), "Upcoming Match")
        tabAdapter.addFragments(LeagueKlasement().newIntance(leagueItem[0].leagueId.toString()), "Klasemen")
        tabAdapter.addFragments(TeamListFragment().newInstance(leagueItem[0].leagueId.toString()), "Teams")

        matchPager.adapter = tabAdapter
        leagueTabMatch.setupWithViewPager(matchPager)
    }

    override fun onDestroy() {
        super.onDestroy()
        mvModel.activityDestroyed()
    }
}
