package com.dicoding.soccer.utilities

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class TabPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
    private var fragtItem: ArrayList<Fragment> = ArrayList()
    private var fragtTitle: ArrayList<String> = ArrayList()

    fun addFragments(fragmentItem: Fragment, fragmentTitle: String) {
        fragtItem.add(fragmentItem)
        fragtTitle.add(fragmentTitle)
    }

    override fun getItem(position: Int): Fragment = fragtItem[position]

    override fun getCount(): Int = fragtItem.size

    override fun getPageTitle(position: Int): CharSequence? = fragtTitle[position]

}