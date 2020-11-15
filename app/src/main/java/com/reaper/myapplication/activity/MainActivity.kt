package com.reaper.myapplication.activity

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.transition.Slide
import androidx.transition.Transition
import androidx.transition.TransitionManager
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.reaper.myapplication.R
import com.reaper.myapplication.adapter.ViewPagerAdapter
import com.reaper.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var tablayout: TabLayout
    lateinit var viewPager: ViewPager
    lateinit var viewPagerAdapter: ViewPagerAdapter
    lateinit var onlineEllipse:ImageView
    lateinit var dragUpButton:ImageView
    lateinit var onlinePlay:ImageView
    lateinit var txtSongName:TextView
    lateinit var txtDuration:TextView
    lateinit var dragDownButton:ImageView
    lateinit var relativeGroup:RelativeLayout
    lateinit var title:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title=binding.titleMain

        onlineEllipse=binding.onlineEllipse
        onlineEllipse.visibility=View.GONE

        dragUpButton=binding.dragUpButton
        onlinePlay=binding.onlinePlay
        onlinePlay.visibility=View.INVISIBLE

        txtSongName=binding.txtSongName
        txtSongName.visibility=View.INVISIBLE

        txtDuration=binding.txtDuration
        txtDuration.visibility=View.INVISIBLE

        dragDownButton=binding.dragDownButton
        dragDownButton.visibility=View.INVISIBLE

        viewPager=binding.mainViewPager
        viewPagerAdapter= ViewPagerAdapter(supportFragmentManager,title)

        viewPager.adapter=viewPagerAdapter
        tablayout=binding.mainTabLayout

        relativeGroup=binding.relateiveGroup

        tablayout.setupWithViewPager(viewPager)

        dragUpButton.setOnClickListener {
                dragUpButton.animate().apply {
                    duration = 500
                    rotationXBy(360f)
                }.withEndAction {
                    val transition: Transition = Slide(Gravity.BOTTOM)
                    transition.duration = 300
                    transition.addTarget(R.id.onlineEllipse)
                    transition.addTarget(R.id.onlinePlay)
                    transition.addTarget(R.id.txtSongName)
                    transition.addTarget(R.id.txtDuration)
                    transition.addTarget(R.id.dragDownButton)
                    TransitionManager.beginDelayedTransition(relativeGroup, transition)
                    dragUpButton.visibility=View.GONE
                    onlineEllipse.visibility = View.VISIBLE
                    onlinePlay.visibility = View.VISIBLE
                    txtSongName.visibility = View.VISIBLE
                    txtDuration.visibility = View.VISIBLE
                    dragDownButton.visibility = View.VISIBLE
                }
        }

        dragDownButton.setOnClickListener {
            dragDownButton.animate().apply {
                duration=500
                rotationXBy(360f)
            }.withEndAction {
                val transition: Transition = Slide(Gravity.BOTTOM)
                transition.duration = 300
                transition.addTarget(R.id.onlineEllipse)
                transition.addTarget(R.id.onlinePlay)
                transition.addTarget(R.id.txtSongName)
                transition.addTarget(R.id.txtDuration)
                transition.addTarget(R.id.dragDownButton)
                TransitionManager.beginDelayedTransition(relativeGroup, transition)
                dragUpButton.visibility=View.VISIBLE
                onlineEllipse.visibility=View.INVISIBLE
                onlinePlay.visibility=View.INVISIBLE
                txtSongName.visibility=View.INVISIBLE
                txtDuration.visibility=View.INVISIBLE
                dragDownButton.visibility=View.INVISIBLE
            }
        }

        setUpTabIcons()

    }

    private fun setUpTabIcons(){
        tablayout.getTabAt(0)?.setIcon(R.drawable.mysongs)
        tablayout.getTabAt(1)?.setIcon(R.drawable.allsongs)
        tablayout.getTabAt(2)?.setIcon(R.drawable.favourites)
        tablayout.getTabAt(3)?.setIcon(R.drawable.addtoplaylist)
    }

}