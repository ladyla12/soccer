package com.dicoding.soccer.module.search

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.soccer.R
import com.dicoding.soccer.db.model.Match
import com.dicoding.soccer.module.match.MatchInterface
import com.dicoding.soccer.module.match.detail.DetailMatchActivity
import com.dicoding.soccer.utilities.*
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity(), MatchInterface {
    companion object {
        var query: String = "QUERY"
    }

    private var matchItem: MutableList<Match> = mutableListOf()
    private var team: String? = null
    private lateinit var svModel: SearchViewModel
    private lateinit var listMatchAdapter: ListMatchAdapter
    private lateinit var src: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        team = intent.getStringExtra(query)

        super.onCreate(savedInstanceState)
        svModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)
        setContentView(R.layout.activity_search)
        setSupportActionBar(src_app_bar)
        val titleSupport = supportActionBar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        titleSupport?.title = "Search Match"

        /*src = src_bar
        src.queryHint = "Enter the Team Name"
        src.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                svModel.loadMatch(applicationContext, query.toString(), this@SearchActivity)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { svModel.loadMatch(applicationContext, it, this@SearchActivity) }
                Log.d("XXX", "$newText")
                return false
            }
        })*/

        svModel.loadMatch(applicationContext, team.toString(), this)

        listMatchAdapter = ListMatchAdapter(this, matchItem, this){}
        src_recyview.layoutManager = LinearLayoutManager(applicationContext)
        src_recyview.adapter = listMatchAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.src_menu, menu)
        val src = menu?.findItem(R.id.src_bar)?.actionView as SearchView
        src.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { svModel.loadMatch(applicationContext, it, this@SearchActivity) }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { svModel.loadMatch(applicationContext, it, this@SearchActivity) }
                Log.d("XXX", "From Activity : $newText")
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

    override fun loadData(data: List<Match>) {
        matchItem.addAll(data)
        listMatchAdapter.notifyDataSetChanged()
    }

    override fun whenItemSelected(position: Int) {
        val intent = Intent(applicationContext, DetailMatchActivity::class.java)
        intent.putExtra(DetailMatchActivity.eventId, matchItem[position].eventId)
        intent.putExtra(DetailMatchActivity.team_home_id, matchItem[position].homeTeamId)
        intent.putExtra(DetailMatchActivity.team_away_id, matchItem[position].awayTeamId)
        startActivity(intent)
        finish()
    }
}
