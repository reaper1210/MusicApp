package com.reaper.myapplication.activity

import android.content.Intent
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
import com.reaper.myapplication.MusicApplication
import com.reaper.myapplication.R
import com.reaper.myapplication.adapter.ViewPagerAdapter
import com.reaper.myapplication.databinding.ActivityMainBinding
import com.reaper.myapplication.utils.MySongInfo
import com.reaper.myapplication.utils.OnlineSongsInfo

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var tablayout: TabLayout
    lateinit var viewPager: ViewPager
    lateinit var viewPagerAdapter: ViewPagerAdapter
    lateinit var onlineEllipse:ImageView
    lateinit var dragUpButton:ImageView
    lateinit var onlinePlay:ImageView
    lateinit var onlinePause:ImageView
    lateinit var txtSongName:TextView
    lateinit var txtSongArtist:TextView
    lateinit var dragDownButton:ImageView
    lateinit var relativeGroup:RelativeLayout
    lateinit var title:TextView
    lateinit var applic:MusicApplication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        applic = this.application as MusicApplication
        applic.mainActivity = this@MainActivity

        title=binding.titleMain

        onlineEllipse=binding.onlineEllipse
        onlineEllipse.visibility=View.GONE

        dragUpButton=binding.dragUpButton
        onlinePlay=binding.onlinePlay
        onlinePause=binding.onlinePause
        onlinePlay.visibility=View.GONE
        onlinePause.visibility=View.GONE

        txtSongName=binding.txtSongName
        txtSongName.isSelected
        txtSongName.visibility=View.INVISIBLE

        txtSongArtist=binding.txtArtist
        txtSongArtist.visibility=View.INVISIBLE

        dragDownButton=binding.dragDownButton
        dragDownButton.visibility=View.INVISIBLE

        viewPager=binding.mainViewPager
        viewPagerAdapter= ViewPagerAdapter(supportFragmentManager,title)

        viewPager.adapter=viewPagerAdapter
        tablayout=binding.mainTabLayout

        relativeGroup=binding.relativeGroup

        tablayout.setupWithViewPager(viewPager)

        when {
            applic.currentOnlineSongsInfo!=null -> {
                txtSongName.text = applic.currentOnlineSongsInfo?.name
                txtSongName.isSelected = true
                txtSongArtist.text = applic.currentOnlineSongsInfo?.artist
            }
            applic.currentMySongInfo!=null -> {
                txtSongName.text = applic.currentMySongInfo?.name
                txtSongName.isSelected = true
                val artist = applic.currentMySongInfo?.artist
                txtSongArtist.text = artist
            }
            else -> {
                txtSongName.text = "Mein hu Gian!!"
                txtSongArtist.text = "Gian"
                onlinePause.visibility = View.GONE
            }
        }

        dragUpButton.setOnClickListener {
                dragUpButton.animate().apply {
                    duration = 300
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
                    txtSongName.visibility = View.VISIBLE
                    txtSongName.isSelected = true
                    txtSongArtist.visibility = View.VISIBLE
                    onlineEllipse.visibility = View.VISIBLE
                    if(applic.musicIsPlaying){
                        onlinePlay.visibility=View.VISIBLE
                        onlinePause.visibility=View.GONE
                    }
                    else{
                        onlinePlay.visibility=View.GONE
                        onlinePause.visibility=View.VISIBLE
                    }
                    dragDownButton.visibility = View.VISIBLE
                }
        }

        dragDownButton.setOnClickListener {
            dragDownButton.animate().apply {
                duration=300
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
                txtSongName.visibility=View.GONE
                txtSongArtist.visibility=View.GONE
                dragUpButton.visibility=View.VISIBLE
                onlineEllipse.visibility=View.GONE
                onlinePlay.visibility=View.GONE
                onlinePause.visibility= View.GONE
                dragDownButton.visibility=View.GONE
            }
        }

        onlinePlay.setOnClickListener {
            onlinePlay.visibility=View.GONE
            onlinePause.visibility=View.VISIBLE
            applic.mediaPlayer.pause()
            applic.musicIsPlaying = false
        }

        onlinePause.setOnClickListener {
            onlinePause.visibility=View.GONE
            onlinePlay.visibility= View.VISIBLE
            applic.mediaPlayer.start()
            applic.musicIsPlaying = true
        }

        onlineEllipse.setOnClickListener {
            dragDownButton.callOnClick()
            val intent = Intent(this@MainActivity, SongActivity::class.java)
            intent.putExtra("isLoaded",true)
            startActivity(intent)
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