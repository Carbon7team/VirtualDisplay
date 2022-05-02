package com.carbon7.virtualdisplay

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.carbon7.virtualdisplay.databinding.ActivityMainBinding

import android.widget.Switch
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.carbon7.virtualdisplay.model.DataManager
import com.carbon7.virtualdisplay.ui.alarms.AlarmsFragment
import com.carbon7.virtualdisplay.ui.diagram.DiagramFragment
import com.carbon7.virtualdisplay.ui.status.StatusFragment
import com.carbon7.virtualdisplay.ui.ups_selector.UpsSelectorFragment


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawer: DrawerLayout
    private lateinit var toolbar: Toolbar

    private var currentFragment = R.layout.fragment_diagram

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbar = binding.appToolbar.toolbar
        drawer = binding.drawerLayout

        setSupportActionBar(toolbar)


        toggle = ActionBarDrawerToggle(
            this,
            drawer,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()


        binding.navView.setNavigationItemSelectedListener {
            it.isChecked = true
            drawer.closeDrawer(Gravity.LEFT)
            val m = DataManager()

            if (it.itemId == R.id.diagram_menu_item) {
                changeFragment(DiagramFragment())
            } else if (it.itemId == R.id.alarms_menu_item) {
                changeFragment(AlarmsFragment())
            } else {
                changeFragment(StatusFragment())
            }

            Toast.makeText(this, it.title, Toast.LENGTH_SHORT).show()
            true
        }

        if(savedInstanceState == null) {
            changeFragment(DiagramFragment())
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
            changeFragment(UpsSelectorFragment())
            true
        }
        else -> {
            super.onOptionsItemSelected(item)

        }
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        //outState.put
        //outState.putInt("currentFragment",)
    }

    private fun changeFragment(newFrag: Fragment){
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()

        transaction.replace(R.id.fragment_container, newFrag);
        transaction.addToBackStack(null);

        transaction.commit()
    }
}