package com.dicoding.soccer.module.team.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.dicoding.soccer.R
import com.dicoding.soccer.db.RestApiClient
import com.dicoding.soccer.db.model.Team
import com.dicoding.soccer.db.model.TeamResponse
import com.dicoding.soccer.db.repository.ApiRepository
import com.dicoding.soccer.module.team.TeamInterface
import kotlinx.android.synthetic.main.team_info_fragment.*

class TeamInfoFragment : Fragment(), TeamInterface {

    companion object {
        const val idTeam: String = "TEAM_ID"
    }

    private var teamId: String? = null
    private var facebook: String = "https://www.facebook.com"
    private var twitter: String = "https://www.twitter.com"
    private var youtube: String = "https://www.youtube.com"
    private var unknown: String = "Unknown"
    private var teamInfoList: MutableList<Team> = mutableListOf()
    private lateinit var viewModel: TeamInfoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        teamId = arguments?.getString(idTeam)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.team_info_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(TeamInfoViewModel::class.java)

        teamInfoList.clear()
        viewModel.fragmentCreated(this, ApiRepository())
        if (RestApiClient.networkCheck(requireContext())){
            teamId?.let { viewModel.loadTeam(it) }
        }
    }

    override fun showLoading() {}
    override fun hideLoading() {}
    override fun whenItemSelected(position: Int) {}

    override fun showMessage(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }

    override fun loadData(data: TeamResponse) {
        teamInfoList.clear()
        teamInfoList.addAll(data.teams)

        if (teamInfoList[0].teamFormedYear.isNullOrEmpty()){
            info_formed.text = unknown
        }
        else {
            info_formed.text = teamInfoList[0].teamFormedYear
        }

        if (teamInfoList[0].teamFacebook.isNullOrBlank() && teamInfoList[0].teamTwitter.isNullOrBlank() && teamInfoList[0].teamYoutube.isNullOrBlank()){
            info_fb_link.text = facebook
            info_twitter_link.text = twitter
            info_youtube_link.text = youtube
        }
        else if(teamInfoList[0].teamFacebook.isNullOrBlank()){
            info_fb_link.text = facebook
            info_twitter_link.text = teamInfoList[0].teamTwitter
            info_youtube_link.text = teamInfoList[0].teamYoutube
        }
        else if (teamInfoList[0].teamTwitter.isNullOrBlank()){
            info_fb_link.text = teamInfoList[0].teamFacebook
            info_twitter_link.text = twitter
            info_youtube_link.text = teamInfoList[0].teamYoutube
        }
        else if (teamInfoList[0].teamYoutube.isNullOrBlank()){
            info_fb_link.text = teamInfoList[0].teamFacebook
            info_twitter_link.text = teamInfoList[0].teamTwitter
            info_youtube_link.text = youtube
        }
        else {
            info_fb_link.text = teamInfoList[0].teamFacebook
            info_twitter_link.text = teamInfoList[0].teamTwitter
            info_youtube_link.text = teamInfoList[0].teamYoutube
        }

        if (teamInfoList[0].teamDesc.isNullOrEmpty()){
            info_desc.text = unknown
        }
        else {
            info_desc.text = teamInfoList[0].teamDesc
        }
    }

    fun newIntance(teamId: String): TeamInfoFragment {
        val fragment = TeamInfoFragment()
        val args = Bundle()
        args.putString(idTeam, teamId)
        fragment.arguments = args

        return fragment
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.fragmentDestroyed()
    }
}
