package com.reaper.myapplication.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.reaper.myapplication.fragment.Favourites
import com.reaper.myapplication.fragment.MySongs
import com.reaper.myapplication.fragment.OnlineSongs
import com.reaper.myapplication.fragment.Playlists

class ViewPagerAdapter(fm: FragmentManager): FragmentStatePagerAdapter(fm) {
    override fun getCount(): Int {
        return 4
    }

    override fun getItem(position: Int): Fragment {
        val pos = position+1
        val fragment: Fragment
        fragment = when(pos){

            1->{
                OnlineSongs()
            }
            2->{
                MySongs()
            }
            3->{
                Favourites()
            }
            else->{
                Playlists()
            }

        }
        return fragment
    }


}