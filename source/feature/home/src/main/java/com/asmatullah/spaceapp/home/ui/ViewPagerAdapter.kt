package com.asmatullah.spaceapp.home.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class ViewPagerAdapter(manager: FragmentManager) : FragmentStatePagerAdapter(manager) {
    private var itemList: MutableList<Fragment> = ArrayList()
    private var titleList: MutableList<String> = ArrayList()

    override fun getItem(position: Int): Fragment {
        return itemList[position]
    }

    fun addFragment(fragment: Fragment, title: String) {
        itemList.add(fragment)
        titleList.add(title)
        notifyDataSetChanged()
    }

    override fun getCount(): Int = itemList.size

    override fun getItemPosition(objects: Any): Int {
        return itemList.indexOf(objects)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titleList[position]
    }
}