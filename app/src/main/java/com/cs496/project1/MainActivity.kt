package com.cs496.project1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    // Push branch test
    var tabTitle = arrayOf("CONTACTS", "IMAGES", "MUSIC")
    lateinit var swipeRefreshLayout : SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var pager = findViewById<ViewPager2>(R.id.viewPager2)
        val t1 = findViewById<TabLayout>(R.id.tabLayout2)
        pager.adapter = TabAdapter(supportFragmentManager, lifecycle)

        TabLayoutMediator(t1, pager) {
            tab, position ->
                tab.text = tabTitle[position]
        }.attach()

        swipeRefreshLayout = findViewById<SwipeRefreshLayout>(R.id.refresh_layout)
        swipeRefreshLayout.setOnRefreshListener {
            this.recreate()
            swipeRefreshLayout.isRefreshing = false
        }
    }

}