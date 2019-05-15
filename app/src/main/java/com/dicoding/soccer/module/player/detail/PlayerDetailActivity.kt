package com.dicoding.soccer.module.player.detail

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.dicoding.soccer.R
import com.dicoding.soccer.db.RestApiClient
import com.dicoding.soccer.db.model.Player
import com.dicoding.soccer.db.model.PlayerDetailResponse
import com.dicoding.soccer.db.model.PlayerResponse
import com.dicoding.soccer.db.repository.ApiRepository
import com.dicoding.soccer.module.player.PlayerInterface
import com.dicoding.soccer.utilities.dateFormat
import com.dicoding.soccer.utilities.invicible
import com.dicoding.soccer.utilities.visible
import kotlinx.android.synthetic.main.activity_player_detail.*

class PlayerDetailActivity : AppCompatActivity(), PlayerInterface {

    companion object {
        const val PLAYER_ID = "PLAYER_ID"
    }

    private var playerId: String? = null
    private var facebook: String = "https://www.facebook.com"
    private var twitter: String = "https://www.twitter.com"
    private var instagram: String = "https://www.instagram.com"
    private var unknown: String = "Unknown"
    private var playerData: MutableList<Player> = mutableListOf()
    private lateinit var pdModel: PlayerDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        playerId = intent.getStringExtra(PLAYER_ID)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player_detail)

        pdModel = ViewModelProviders.of(this).get(PlayerDetailViewModel::class.java)

        setSupportActionBar(dtl_player_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Detail Player"

        playerData.clear()
        pdModel.onActivityCreated(this, ApiRepository())
        if (RestApiClient.networkCheck(applicationContext)){
            playerId?.let { pdModel.loadPlayers(it) }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun showLoading() {
        player_pg.visible()
    }

    override fun hideLoading() {
        player_pg.invicible()
    }

    override fun showMessage(msg: String) {
        Toast.makeText(applicationContext, msg, Toast.LENGTH_LONG).show()
    }

    override fun loadData(data: PlayerResponse) {}

    override fun loadDetail(data: PlayerDetailResponse) {
        playerData.clear()
        playerData.addAll(data.players)

        if (playerData[0].strThumb.isNullOrEmpty()){
            Glide.with(this)
                .load(R.drawable.no_image_player)
                .into(dtl_player_img)
        }
        else {
            Glide.with(this)
                .load(playerData[0].strThumb)
                .into(dtl_player_img)
        }

        if (playerData[0].strPlayer.isNullOrEmpty()){
            dtl_player_name.text = unknown
        }
        else {
            dtl_player_name.text = playerData[0].strPlayer
        }

        if (playerData[0].strFacebook.isNullOrEmpty() && playerData[0].strTwitter.isNullOrEmpty() && playerData[0].strInstagram.isNullOrEmpty()){
            dtl_player_facebook.text = facebook
            dtl_player_twitter.text = twitter
            dtl_player_instagram.text = instagram
        }
        else if (playerData[0].strFacebook.isNullOrEmpty()){
            dtl_player_facebook.text = facebook
            dtl_player_twitter.text = playerData[0].strTwitter
            dtl_player_instagram.text = playerData[0].strInstagram
        }
        else if (playerData[0].strTwitter.isNullOrEmpty()) {
            dtl_player_facebook.text = playerData[0].strFacebook
            dtl_player_twitter.text = twitter
            dtl_player_instagram.text = playerData[0].strInstagram
        }
        else if (playerData[0].strInstagram.isNullOrEmpty()){
            dtl_player_facebook.text = playerData[0].strFacebook
            dtl_player_twitter.text = playerData[0].strTwitter
            dtl_player_instagram.text = instagram
        }
        else {
            dtl_player_facebook.text = playerData[0].strFacebook
            dtl_player_twitter.text = playerData[0].strTwitter
            dtl_player_instagram.text = playerData[0].strInstagram
        }

        if (playerData[0].dateBorn.isNullOrEmpty()){
            dtl_player_birthday.text = unknown
        }
        else {
            dtl_player_birthday.text = dateFormat(playerData[0].dateBorn)
        }

        if (playerData[0].strNationality.isNullOrEmpty()){
            dtl_player_nationality.text = unknown
        }
        else {
            dtl_player_nationality.text = playerData[0].strNationality
        }

        if (playerData[0].strHeight.isNullOrEmpty()){
            dtl_player_height.text = unknown
        }
        else {
            dtl_player_height.text = playerData[0].strHeight
        }

        if (playerData[0].strWeight.isNullOrEmpty()){
            dtl_player_weight.text = unknown
        }
        else {
            dtl_player_weight.text = playerData[0].strWeight
        }

        if (playerData[0].dateSigned.isNullOrEmpty()){
            dtl_player_signdate.text = unknown
        }
        else {
            dtl_player_signdate.text = dateFormat(playerData[0].dateSigned)
        }

        if (playerData[0].strSigning.isNullOrEmpty()){
            dtl_player_signprice.text = unknown
        }
        else {
            dtl_player_signprice.text = playerData[0].strSigning
        }

        if (playerData[0].strPosition.isNullOrEmpty()){
            dtl_player_position.text = unknown
        }
        else {
            dtl_player_position.text = playerData[0].strPosition
        }

        if (playerData[0].strDescriptionEN.isNullOrEmpty()){
            dtl_player_desc.text = unknown
        }
        else {
            dtl_player_desc.text = playerData[0].strDescriptionEN
        }
    }

    override fun whenDataSelected(position: Int) {}

    override fun onDestroy() {
        super.onDestroy()
        pdModel.onActivityDestroyed()
    }
}
