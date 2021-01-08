package com.asmatullah.spaceapp.home.ui

import android.view.MenuItem
import androidx.viewpager.widget.ViewPager
import com.asmatullah.spaceapp.common.core.ui.BaseFragment
import com.asmatullah.spaceapp.home.R
import com.asmatullah.spaceapp.home.databinding.FragmentHomeBinding
import com.asmatullah.spaceapp.home.ui.favorites.FavoritesFragment
import com.asmatullah.spaceapp.home.ui.stations.StationsFragment
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    override fun configureUI() {
        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addFragment(StationsFragment(), getString(R.string.stations))
        adapter.addFragment(FavoritesFragment(), getString(R.string.favorites))
        viewPager.adapter = adapter

        bottomNavigation.setOnNavigationItemSelectedListener(::onItemClickListener)
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                val id = when (position) {
                    0 -> R.id.itemStations
                    1 -> R.id.itemFavorites
                    else -> R.id.itemStations
                }
                bottomNavigation.menu.findItem(id).isChecked = true
            }
        })
    }

    private fun onItemClickListener(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.itemStations -> viewPager.currentItem = 0
            R.id.itemFavorites -> viewPager.currentItem = 1
        }
        return true
    }
}