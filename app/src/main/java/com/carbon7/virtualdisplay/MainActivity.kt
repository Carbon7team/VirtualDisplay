package com.carbon7.virtualdisplay

import android.content.*
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.IBinder
import android.util.AttributeSet
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.GravityCompat
import androidx.core.view.get
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.carbon7.virtualdisplay.databinding.ActivityMainBinding
import com.carbon7.virtualdisplay.model.UpsDataService
import com.carbon7.virtualdisplay.ui.alarms.AlarmsFragment
import com.carbon7.virtualdisplay.ui.diagram.DiagramFragment
import com.carbon7.virtualdisplay.ui.status.StatusFragment
import kotlin.reflect.KClass


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawer: DrawerLayout
    private lateinit var toolbar: Toolbar
    private var backTimestamp = System.currentTimeMillis() - 1000


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbar = binding.appToolbar.toolbar
        drawer = binding.drawerLayout

        setSupportActionBar(toolbar)


        toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()



        binding.navView.setNavigationItemSelectedListener {
            drawer.closeDrawer(GravityCompat.START)

            when(it.itemId){
                R.id.diagram_menu_item -> changeFragment(DiagramFragment::class)
                R.id.status_menu_item -> changeFragment(StatusFragment::class)
                R.id.alarms_menu_item -> changeFragment(AlarmsFragment::class)
            }

            Toast.makeText(this, it.title, Toast.LENGTH_SHORT).show()
            true
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)

        toolbar.menu.findItem(R.id.ups_conn_status).isVisible = true
        toolbar.menu.findItem(R.id.ups_conn_status).icon = ResourcesCompat.getDrawable(resources,R.drawable.ic_ups_disconnected,null)

        /*val toggleservice = menu!!.findItem(R.id.app_bar_switch)
        val actionView = toggleservice.actionView.findViewById(R.id.switch_toolbar) as Switch
        actionView.setOnCheckedChangeListener { compoundButton, b ->
            if(b) {
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                toolbar.navigationIcon = null
            }else{
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                toggle.syncState()
            }
        }*/

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


    override fun onBackPressed() {
        if(binding.drawerLayout.isDrawerOpen(GravityCompat.START))
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        else {
            if(supportFragmentManager.backStackEntryCount>0){
                super.onBackPressed()
                when(binding.appToolbar.fragmentContainer.getFragment<Fragment>()){
                    is DiagramFragment -> binding.navView.setCheckedItem(R.id.diagram_menu_item)
                    is StatusFragment -> binding.navView.setCheckedItem(R.id.status_menu_item)
                    is AlarmsFragment -> binding.navView.setCheckedItem(R.id.alarms_menu_item)
                }
            }else {
                if(System.currentTimeMillis() - backTimestamp > 1000) {
                    backTimestamp = System.currentTimeMillis()
                    Toast.makeText(this, "Press Again to Exit", Toast.LENGTH_SHORT).show()
                }else
                    super.onBackPressed()
            }
        }
    }

    private fun changeFragment(fragClass: KClass<out Fragment>){

        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()

        val frag = supportFragmentManager.findFragmentByTag(fragClass.simpleName)
        if(frag==null)
            transaction.replace(R.id.fragment_container, fragClass.java, null, fragClass.simpleName)
        else
            transaction.replace(R.id.fragment_container, frag, fragClass.simpleName)

        transaction.addToBackStack(null);
        transaction.commit()



    }



    val alert by lazy {
        AlertDialog.Builder(this)
            .setTitle("Connessione interrotta")
            .setMessage("La connessione con l'UPS sì è interrotta come procedere?")
            .setPositiveButton("Esci") { dialogInterface, i -> TODO("Transizione a StartingActivity") }
            .setNegativeButton("Attendi") { dialogInterface, i -> }
            .create()
    }
    private lateinit var mService: UpsDataService
    private var lastConnState = UpsDataService.ConnectionState.DISCONNECTED
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            mService= (service as UpsDataService.LocalBinder).getService()
            mService.connectionStateBus.events.observe(this@MainActivity){
                if(it != lastConnState){
                    toolbar.menu.findItem(R.id.ups_conn_status).icon = when(it){
                        UpsDataService.ConnectionState.DISCONNECTED -> ResourcesCompat.getDrawable(resources,R.drawable.ic_ups_disconnected,null)
                        UpsDataService.ConnectionState.CONNECTED -> ResourcesCompat.getDrawable(resources,R.drawable.ic_ups_connected,null)
                    }
                    if(it==UpsDataService.ConnectionState.DISCONNECTED)
                        alert.show()
                    lastConnState=it
                }

            }
        }
        override fun onServiceDisconnected(p0: ComponentName?) {

        }
    }
    override fun onStop() {
        super.onStop()
        stopService(Intent(this,UpsDataService::class.java))
    }

    override fun onStart() {
        super.onStart()
        Intent(this, UpsDataService::class.java).apply {
            putExtra("ip","192.168.11.178")
            putExtra("port",8888)
            putExtra("interval",1000L)
        }.also {
            startService(it)
        }

        bindService(Intent(this, UpsDataService::class.java),connection,0)


    }

}