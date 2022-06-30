package com.cs496.project1

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PackageManagerCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    var tabTitle = arrayOf("CONTACTS", "IMAGES", "MUSIC")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //* Block 1: Check Permissions
        val status = ContextCompat.checkSelfPermission(this, "android.permission.READ_CONTACTS")
        if(status == PackageManager.PERMISSION_GRANTED) {
            Log.d("test", "permission granted")
        } else {
            ActivityCompat.requestPermissions(this, arrayOf<String>("android.permission.READ_CONTACTS"), 100)
            Log.d("test", "permission denied")
        }
        //*/

        var pager = findViewById<ViewPager2>(R.id.viewPager2)
        val t1 = findViewById<TabLayout>(R.id.tabLayout2)
        pager.adapter = TabAdapter(supportFragmentManager, lifecycle)

        TabLayoutMediator(t1, pager) {
            tab, position ->
                tab.text = tabTitle[position]
        }.attach()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d("test", "permission granted")
        } else {
            Log.d("test", "permission denied")
        }
    }
}