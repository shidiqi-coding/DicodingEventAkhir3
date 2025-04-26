package com.dicoding.dicodingevent

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

import androidx.viewpager2.widget.ViewPager2

import com.dicoding.dicodingevent.databinding.ActivityMainBinding

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.MainToolbar)

        val viewPager: ViewPager2 = binding.viewPager
        val navView: BottomNavigationView = binding.navView

       
        val adapter = ViewPagerAdapter(this)
        viewPager.adapter = adapter

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                navView.menu.getItem(position).isChecked = true
            }
        })

        navView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> viewPager.currentItem = 0
                R.id.navigation_upcoming -> viewPager.currentItem = 1
                R.id.navigation_finished -> viewPager.currentItem = 2
                R.id.navigation_favorite -> viewPager.currentItem = 3
                R.id.navigation_setting -> viewPager.currentItem = 4
            }
            true
        }

        val destination = intent.getStringExtra("destination")
        if (destination == "upcoming") {
            viewPager.currentItem = 1
        }


    }
}
