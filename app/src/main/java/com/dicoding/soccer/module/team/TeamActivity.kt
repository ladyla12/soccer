package com.dicoding.soccer.module.team

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dicoding.soccer.MainActivity
import com.dicoding.soccer.R
import com.dicoding.soccer.db.database
import com.dicoding.soccer.module.favorite.FavoriteActivity
import com.dicoding.soccer.module.player.list.PlayerListFragment
import com.dicoding.soccer.module.team.info.TeamInfoFragment
import com.dicoding.soccer.utilities.FavoriteTeam
import com.dicoding.soccer.utilities.TabPagerAdapter
import com.google.android.gms.security.ProviderInstaller
import kotlinx.android.synthetic.main.activity_team.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import java.sql.SQLClientInfoException

class TeamActivity : AppCompatActivity() {

    companion object {
        var teamId: String = "TEAM_ID"
        var teamName: String = "TEAM_NAME"
        var teamBadge: String = "TEAM_BADGE"
    }

    private var idTeam: String? = null
    private var nameTeam: String? = null
    private var badgeTeam: String? = null
    private var isFavorite: Boolean = false
    private var menuItem: Menu? = null
    private lateinit var tabAdapter: TabPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        idTeam = intent.getStringExtra(teamId)
        nameTeam = intent.getStringExtra(teamName)
        badgeTeam = intent.getStringExtra(teamBadge)

        favoriteState()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team)
        setSupportActionBar(dtl_team_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Detail Team"

        try {
            ProviderInstaller.installIfNeeded(applicationContext)
        } catch (e: Exception) {
            Toast.makeText(applicationContext, "${e.message}", Toast.LENGTH_LONG).show()
        }

        Glide.with(this)
            .load(badgeTeam)
            .into(dtl_team_badge)

        dtl_team_name.text = nameTeam

        tabAdapter = TabPagerAdapter(supportFragmentManager)
        tabAdapter.addFragments(TeamInfoFragment().newIntance(idTeam.toString()), "Team Info")
        tabAdapter.addFragments(PlayerListFragment().newInstance(idTeam.toString()), "Team Players")

        teamPager.adapter = tabAdapter
        team_tab.setupWithViewPager(teamPager)
    }

    private fun setFavorite() {
        if (isFavorite) {
            menuItem?.findItem(R.id.dtl_favorite)?.setIcon(R.drawable.ic_favorite_border)
        } else {
            menuItem?.findItem(R.id.dtl_favorite)?.setIcon(R.drawable.ic_favorite)
        }
    }

    private fun addToFavorite() {
        try {
            database.use {
                insert(
                    FavoriteTeam.TEAM_FAVORITE,
                    FavoriteTeam.TEAM_ID to idTeam,
                    FavoriteTeam.TEAM_NAME to nameTeam,
                    FavoriteTeam.TEAM_BADGE to badgeTeam
                )
            }
            Toast.makeText(applicationContext, "Added to Database", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(applicationContext, e.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }

    private fun removeFromFavorite() {
        try {
            database.use {
                delete(
                    FavoriteTeam.TEAM_FAVORITE, "(TEAM_ID = {id})", "id" to idTeam.toString()
                )
            }
            Toast.makeText(applicationContext, "Removed from Database", Toast.LENGTH_LONG).show()
        } catch (e: SQLClientInfoException) {
            Toast.makeText(applicationContext, e.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }

    private fun favoriteState() {
        try {
            database.use {
                val result = select(FavoriteTeam.TEAM_FAVORITE)
                    .whereArgs("(TEAM_ID = {id})", "id" to idTeam.toString())
                val fav = result.parseList(classParser<FavoriteTeam>())
                if (fav.isNotEmpty()) isFavorite = true
            }
        } catch (e: SQLClientInfoException) {
            Toast.makeText(applicationContext, e.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.fav_menu, menu)
        if (isFavorite) {
            menu?.findItem(R.id.dtl_favorite)?.setIcon(R.drawable.ic_favorite)
        } else {
            menu?.findItem(R.id.dtl_favorite)?.setIcon(R.drawable.ic_favorite_border)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
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
                } else {
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
}
