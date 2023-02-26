package com.example.os

//import android.support.v7.app.AppCompatActivity
import android.os.Bundle
////import android.support.design.widget.NavigationView
//import android.support.v4.widget.DrawerLayout
//import android.support.v7.app.ActionBar
//import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout  = findViewById(R.id.drower_layout)
        val navView : NavigationView = findViewById(R.id.nav)

        toggle = ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {
            it.isChecked = true
            when(it.itemId){
                R.id.home -> replaceFragment(DailyFragment(), it.title.toString())
                R.id.star -> replaceFragment(StarFragment(), it.title.toString())
                R.id.checklist -> replaceFragment(ChecklistFragment(), it.title.toString())
                R.id.recipes -> replaceFragment(RecipesFragment(), it.title.toString())
                R.id.shop -> replaceFragment(ShopFragment(), it.title.toString())
                R.id.clock -> replaceFragment(ClockFragment(), it.title.toString())
            }
            true
        }
    }
    private fun replaceFragment(fragment: Fragment, title : String){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout,fragment)
        fragmentTransaction.commit()
        drawerLayout.closeDrawers()
        setTitle(title)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}