package com.carbon7.virtualdisplay

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.carbon7.virtualdisplay.databinding.ActivityMainBinding
import android.widget.CompoundButton

import android.widget.Switch








class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawer: DrawerLayout
    private lateinit var toolbar: Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbar = binding.appToolbar.toolbar
        drawer = binding.drawerLayout


        setSupportActionBar(toolbar)
        /*setTitle(R.string.app_name)*/

        toggle = ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )

        drawer.addDrawerListener(toggle)
        toggle.syncState()

        binding.navView.setNavigationItemSelectedListener {
            it.isChecked=true
            drawer.closeDrawer(Gravity.START)
            Toast.makeText(this,it.title,Toast.LENGTH_SHORT).show()
            true
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)

        val toggleservice = menu!!.findItem(R.id.app_bar_switch)
        val actionView = toggleservice.actionView.findViewById(R.id.switch_toolbar) as Switch
        actionView.setOnCheckedChangeListener { compoundButton, b ->
            if(b) {
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                toolbar.setNavigationIcon(null)
            }else{
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                toggle.syncState()
            }
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when(item.itemId){
        R.id.support -> {
            Toast.makeText(this,"ASSISTENZA",Toast.LENGTH_SHORT).show()
            true
        }
        R.id.settings -> {
            Toast.makeText(this,"IMPOSTAZIONI",Toast.LENGTH_SHORT).show()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)

        }
    }
       /* Log.d("MyApp", applicationContext.getString(item.getItemId()))
        Toast.makeText(this,item.itemId,Toast.LENGTH_SHORT).show()
        return true
    }*/
}