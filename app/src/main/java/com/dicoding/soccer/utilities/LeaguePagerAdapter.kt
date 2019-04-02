package com.dicoding.soccer.utilities

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.dicoding.soccer.R
import com.dicoding.soccer.db.model.League
import kotlinx.android.synthetic.main.league_item.view.*

class LeaguePagerAdapter(context: Context, private var listItem: List<League>) : PagerAdapter() {
    private var layoutInflater: LayoutInflater = LayoutInflater.from(context)
    private var facebook: String = context.getString(R.string.sm_facebook)
    private var twitter: String = context.getString(R.string.sm_twitter)
    private var youtube: String = context.getString(R.string.sm_youtube)

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int = listItem.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = layoutInflater.inflate(R.layout.league_item, container, false)

        view.leagueName.text = listItem[position].leagueName
        Glide.with(container)
            .load(listItem[position].leagueBadge)
            .into(view.leagueImg)

        if (listItem[position].leagueFacebook.isNullOrBlank() && listItem[position].leagueTwitter.isNullOrBlank() && listItem[position].leagueYoutube.isNullOrBlank()){
            view.facebook_link.text = facebook
            view.twitter_link.text = twitter
            view.youtube_link.text = youtube
        }
        else if (listItem[position].leagueFacebook.isNullOrBlank()){
            view.facebook_link.text = facebook
            view.twitter_link.text = listItem[position].leagueTwitter
            view.youtube_link.text = listItem[position].leagueYoutube
        }
        else if (listItem[position].leagueTwitter.isNullOrBlank()){
            view.facebook_link.text = listItem[position].leagueFacebook
            view.twitter_link.text = twitter
            view.youtube_link.text = listItem[position].leagueYoutube
        }
        else if (listItem[position].leagueYoutube.isNullOrBlank()){
            view.facebook_link.text = listItem[position].leagueFacebook
            view.twitter_link.text = listItem[position].leagueTwitter
            view.youtube_link.text = youtube
        }
        else {
            view.facebook_link.text = listItem[position].leagueFacebook
            view.twitter_link.text = listItem[position].leagueTwitter
            view.youtube_link.text = listItem[position].leagueYoutube
        }

        container.addView(view, 0)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeViewInLayout(`object` as View)
    }


}