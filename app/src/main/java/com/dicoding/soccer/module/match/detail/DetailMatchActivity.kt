package com.dicoding.soccer.module.match.detail

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.dicoding.soccer.MainActivity
import com.dicoding.soccer.R
import com.dicoding.soccer.db.database
import com.dicoding.soccer.db.model.MatchDetail
import com.dicoding.soccer.db.model.Team
import com.dicoding.soccer.module.favorite.FavoriteActivity
import com.dicoding.soccer.module.match.DetailMatchInterface
import com.dicoding.soccer.utilities.*
import kotlinx.android.synthetic.main.activity_detail_match.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import java.sql.SQLClientInfoException

class DetailMatchActivity : AppCompatActivity(), DetailMatchInterface {
    companion object {
        var eventId: String = "EVENT_ID"
        var team_home_id: String = "TEAM_HOME_ID"
        var team_away_id: String = "TEAM_AWAY_ID"
    }

    private var matchDetail: MutableList<MatchDetail> = mutableListOf()
    private var teamImage: MutableList<Team> = mutableListOf()
    private lateinit var dtlViewModel: DetailViewModel
    private var idEvent: String? = null
    private var homeBadge: String? = null
    private var awayBadge: String? = null
    private var isFavorite: Boolean = false
    private var menuItem: Menu? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        idEvent = intent.getStringExtra(eventId)
        homeBadge = intent.getStringExtra(team_home_id)
        awayBadge = intent.getStringExtra(team_away_id)
        favoriteState()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_match)
        setSupportActionBar(dtl_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        val titleSupport = supportActionBar
        titleSupport?.title = "Detail Match"
        dtlViewModel = ViewModelProviders.of(this).get(DetailViewModel::class.java)
        dtlViewModel.loadMatch(this, idEvent.toString(), this)
    }

    override fun onResume() {
        super.onResume()
        favoriteState()
        setFavorite()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun showLoading() {
        dtl_pb.visible()
    }

    override fun hideLoading() {
        dtl_pb.invicible()
    }

    override fun showMessage(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    override fun loadImage(data: List<Team>, whichTeam: String) {
        teamImage.clear()
        teamImage.addAll(data)

        when (whichTeam) {
            homeBadge -> {
                Glide.with(this)
                    .load(teamImage[0].teamBadge)
                    .into(dtl_home_badge)
            }

            awayBadge -> {
                Glide.with(this)
                    .load(teamImage[0].teamBadge)
                    .into(dtl_away_badge)
            }
        }
    }

    override fun loadData(data: List<MatchDetail>) {
        matchDetail.clear()
        matchDetail.addAll(data)

        if (matchDetail[0].homeScore == null){
            dtl_score_line.gone()
        }

        dtl_match_date.text = dateFormat(matchDetail[0].eventDate)
        //HOME
        dtl_home_score.text = matchDetail[0].homeScore
        dtl_home_name.text = matchDetail[0].homeTeam
        dtl_home_position.text = matchDetail[0].homeFormation
        dtl_home_goals.text = matchDetail[0].homeGoalDetails
        dtl_home_shots.text = matchDetail[0].homeShots
        dtl_home_gk.text = matchDetail[0].homeLineupGoalkeeper
        dtl_home_def.text = matchDetail[0].homeLineupDefense
        dtl_home_mid.text = matchDetail[0].homeLineupMidfield
        dtl_home_atk.text = matchDetail[0].homeLineupForward
        dtl_home_subtitutes.text = matchDetail[0].homeLineupSubstitutes
        //AWAY
        dtl_away_score.text = matchDetail[0].awayScore
        dtl_away_name.text = matchDetail[0].awayTeam
        dtl_away_position.text = matchDetail[0].awayFormation
        dtl_away_goals.text = matchDetail[0].awayGoalDetails
        dtl_away_shots.text = matchDetail[0].awayShots
        dtl_away_gk.text = matchDetail[0].awayLineupGoalkeeper
        dtl_away_def.text = matchDetail[0].awayLineupDefense
        dtl_away_mid.text = matchDetail[0].awayLineupMidfield
        dtl_away_atk.text = matchDetail[0].awayLineupForward
        dtl_away_subtitutes.text = matchDetail[0].awayLineupSubstitutes
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.fav_menu, menu)
        if (isFavorite){
            menu?.findItem(R.id.dtl_favorite)?.setIcon(R.drawable.ic_favorite)
        }
        else {
            menu?.findItem(R.id.dtl_favorite)?.setIcon(R.drawable.ic_favorite_border)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId){
            R.id.dtl_home_menu -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
                true
            }

            R.id.dtl_fav_menu -> {
                val intent = Intent(this, FavoriteActivity::class.java)
                startActivity(intent)
                finish()
                true
            }

            R.id.dtl_favorite -> {
                if (isFavorite) {
                    item.setIcon(R.drawable.ic_favorite_border)
                    removeFromFavorite()
                }
                else {
                    item.setIcon(R.drawable.ic_favorite)
                    addToFavorite()
                }

                isFavorite = !isFavorite
                setFavorite()

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun addToFavorite(){
        try {
            database.use {
                insert(
                    Favorite.TABLE_FAVORITE,
                    Favorite.MATCH_ID to matchDetail[0].eventId,
                    Favorite.MATCH_DATE to dateFormat(matchDetail[0].eventDate),
                    Favorite.HOME_TEAM_ID to matchDetail[0].homeTeamId,
                    Favorite.HOME_TEAM_NAME to matchDetail[0].homeTeam,
                    Favorite.HOME_TEAM_SCORE to matchDetail[0].homeScore,
                    Favorite.AWAY_TEAM_ID to matchDetail[0].awayTeamId,
                    Favorite.AWAY_TEAM_NAME to matchDetail[0].awayTeam,
                    Favorite.AWAY_TEAM_SCORE to matchDetail[0].awayScore
                )
            }
            showMessage("Added to Database")
        }
        catch (e: Exception) {
            showMessage(e.localizedMessage)
        }
    }

    private fun setFavorite(){
        if(isFavorite){
            menuItem?.findItem(R.id.dtl_favorite)?.setIcon(R.drawable.ic_favorite_border)
        }
        else {
            menuItem?.findItem(R.id.dtl_favorite)?.setIcon(R.drawable.ic_favorite)
        }
    }

    private fun removeFromFavorite(){
        try {
            database.use {
                delete(
                    Favorite.TABLE_FAVORITE, "(MATCH_ID = {id})", "id" to idEvent.toString()
                )
            }
            showMessage("Removed from database")
        }
        catch (e: SQLClientInfoException){
            showMessage(e.localizedMessage)
        }
    }

    private fun favoriteState(){
        try {
            database.use {
                val result = select(Favorite.TABLE_FAVORITE)
                    .whereArgs("(MATCH_ID = {id})", "id" to idEvent.toString())
                val fav = result.parseList(classParser<Favorite>())
                if(fav.isNotEmpty()) isFavorite = true
            }
        }
        catch (e: SQLClientInfoException){
            showMessage(e.localizedMessage)
        }
    }
}
