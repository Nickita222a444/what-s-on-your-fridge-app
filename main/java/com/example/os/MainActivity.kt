package com.example.os

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
//import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBar
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import android.widget.Toast
//import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val drawerLayout : DrawerLayout = findViewById(R.id.drower_layout)
        val navView : android.support.design.widget.NavigationView = findViewById(R.id.nav)

        toggle = ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home -> Toast.makeText(applicationContext, "Clcked Home", Toast.LENGTH_SHORT).show()
                R.id.star -> Toast.makeText(applicationContext, "Clcked star", Toast.LENGTH_SHORT).show()
                R.id.checklist -> Toast.makeText(applicationContext, "Clcked chek", Toast.LENGTH_SHORT).show()
                R.id.recipes -> Toast.makeText(applicationContext, "Clcked recip", Toast.LENGTH_SHORT).show()
                R.id.shop -> Toast.makeText(applicationContext, "Clcked shop", Toast.LENGTH_SHORT).show()
                R.id.clock -> Toast.makeText(applicationContext, "Clcked clock", Toast.LENGTH_SHORT).show()
            }
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}