package com.reaper.myapplication.Activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.reaper.myapplication.R
import com.reaper.myapplication.ViewPagerAdapter
import com.reaper.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var tablayout: TabLayout
    lateinit var viewPager: ViewPager
    lateinit var viewPagerAdapter: ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //val intent= Intent(this@MainActivity,SongActivity::class.java)
        //startActivity(intent)

        viewPager=binding.mainViewPager
        viewPagerAdapter= ViewPagerAdapter(supportFragmentManager)
        viewPager.adapter=viewPagerAdapter
        tablayout=binding.mainTabLayout
        tablayout.setupWithViewPager(viewPager)

        setUpTabIcons()

    }

    private fun setUpTabIcons(){
        for(i in 0..3){
            tablayout.getTabAt(i)?.setIcon(R.drawable.play)
        }
    }

}