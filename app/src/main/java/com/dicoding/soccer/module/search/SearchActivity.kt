package com.dicoding.soccer.module.search

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.soccer.R
import com.dicoding.soccer.db.RestApiClient
import com.dicoding.soccer.db.model.Match
import com.dicoding.soccer.db.model.MatchSearchResponse
import com.dicoding.soccer.db.model.Team
import com.dicoding.soccer.db.model.TeamResponse
import com.dicoding.soccer.db.repository.ApiRepository
import com.dicoding.soccer.module.match.detail.DetailMatchActivity
import com.dicoding.soccer.module.team.TeamActivity
import com.dicoding.soccer.utilities.SearchMatchAdapter
import com.dicoding.soccer.utilities.SearchTeamAdapter
import com.dicoding.soccer.utilities.invicible
import com.dicoding.soccer.utilities.visible
import com.google.android.gms.security.ProviderInstaller
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity(), SearchInterface {
    companion object {
        var QUERY: String = "QUERY"
        var REQUEST_CODE: String = "NULL"
    }

    private var matchItem: MutableList<Match> = mutableListOf()
    private var teamItem: MutableList<Team> = mutableListOf()
    private var requestQuery: String? = null
    private var requestCode: Int? = null
    private lateinit var svModel: SearchViewModel
    private lateinit var listMatchAdapter: SearchMatchAdapter
    private lateinit var listTeamAdapter: SearchTeamAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        try {
            ProviderInstaller.installIfNeeded(applicationContext)
        } catch (e: Exception) {
            showMessage("${e.message}")
        }

        requestQuery = intent.getStringExtra(QUERY)
        requestCode = intent.getIntExtra(REQUEST_CODE, 0)

        super.onCreate(savedInstanceState)
        svModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)
        setContentView(R.layout.activity_search)

        setSupportActionBar(src_app_bar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        when(requestCode){
            0 -> {
                supportActionBar?.title = "Search Match"
                searchMatch()
            }

            1 -> {
                supportActionBar?.title = "Search Match"
                searchMatch()
            }

            2 -> {
                supportActionBar?.title = "Search Match"
                searchTeam()
            }

            3 -> {
                supportActionBar?.title = "Search Team"
                searchTeam()
            }
        }
    }

    private fun searchMatch(){
        matchItem.clear()
        svModel.activityCreated(this, ApiRepository())

        if (RestApiClient.networkCheck(applicationContext)) {
            requestQuery?.let { svModel.loadMatch(it) }
        }

        listMatchAdapter = SearchMatchAdapter(applicationContext, matchItem, this) {}
        src_recyview.layoutManager = LinearLayoutManager(applicationContext)
        src_recyview.adapter = listMatchAdapter
    }

    private fun searchTeam(){
        teamItem.clear()
        svModel.activityCreated(this, ApiRepository())

        if (RestApiClient.networkCheck(applicationContext)){
            requestQuery?.let { svModel.loadTeam(it) }
        }

        listTeamAdapter = SearchTeamAdapter(applicationContext, teamItem, this){}
        src_recyview.layoutManager = GridLayoutManager(applicationContext, 2)
        src_recyview.adapter = listTeamAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.src_menu, menu)
        val src = menu?.findItem(R.id.src_bar)?.actionView as SearchView
        src.queryHint = "Enter requestQuery name"
        src.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                if (requestCode == 2 || requestCode == 3) {
                    newText?.let { svModel.loadTeam(it) }
                }
                else {
                    newText?.let { svModel.loadMatch(it) }
                }

                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun showLoading() {
        src_pg.visible()
    }

    override fun hideLoading() {
        src_pg.invicible()
    }

    override fun showMessage(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    override fun searchMatchResult(data: MatchSearchResponse) {
        matchItem.clear()
        matchItem.addAll(data.event)
        listMatchAdapter.notifyDataSetChanged()
    }

    override fun searchTeamResult(data: TeamResponse) {
        teamItem.clear()
        teamItem.addAll(data.teams)
        listTeamAdapter.notifyDataSetChanged()
    }

    override fun whenItemSelected(position: Int) {

        if (requestCode == 2 || requestCode == 3) {
            val intent = Intent(applicationContext, TeamActivity::class.java)
            intent.putExtra(TeamActivity.teamId, teamItem[0].teamId)
            intent.putExtra(TeamActivity.teamName, teamItem[0].teamName)
            intent.putExtra(TeamActivity.teamBadge, teamItem[0].teamBadge)
            startActivity(intent)
            finish()
        }
        else {
            val intent = Intent(applicationContext, DetailMatchActivity::class.java)
            intent.putExtra(DetailMatchActivity.eventId, matchItem[position].eventId)
            intent.putExtra(DetailMatchActivity.team_home_id, matchItem[position].homeTeamId)
            intent.putExtra(DetailMatchActivity.team_away_id, matchItem[position].awayTeamId)
            startActivity(intent)
            finish()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        svModel.activityDestroyed()
    }
}
