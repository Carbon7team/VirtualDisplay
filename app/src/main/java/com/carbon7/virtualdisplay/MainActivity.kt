package com.carbon7.virtualdisplay

import android.content.*
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.GravityCompat
import androidx.core.view.MenuCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.carbon7.virtualdisplay.databinding.ActivityMainBinding
import com.carbon7.virtualdisplay.model.UpsDataFetcherService
import com.carbon7.virtualdisplay.ui.alarms.AlarmsFragment
import com.carbon7.virtualdisplay.ui.diagram.DiagramFragment
import com.carbon7.virtualdisplay.ui.status.StatusFragment
import kotlin.reflect.KClass
import com.carbon7.virtualdisplay.ui.ups_selector.UpsSelectorFragment
import kotlin.properties.Delegates


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawer: DrawerLayout
    private lateinit var toolbar: Toolbar
    private var backTimestamp = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbar = binding.appToolbar.toolbar
        drawer = binding.drawerLayout

        setSupportActionBar(toolbar)

        //setup the drawer layout
        toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()



        binding.navView.setNavigationItemSelectedListener {
            drawer.closeDrawer(GravityCompat.START)

            when(it.itemId){
                R.id.diagram_menu_item -> changeFragment(DiagramFragment::class)
                R.id.status_menu_item -> changeFragment(StatusFragment::class)
                R.id.alarms_menu_item -> changeFragment(AlarmsFragment::class)

                R.id.disconnect_ups_menu_item -> goBackToUpsSelector()
            }

            Toast.makeText(this, it.title, Toast.LENGTH_SHORT).show()
            true
        }
        changeFragment(DiagramFragment::class, false)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)


        //Show the connection state icon
        toolbar.menu.findItem(R.id.ups_conn_status).isVisible = true
        toolbar.menu.findItem(R.id.ups_conn_status).icon = ResourcesCompat.getDrawable(resources,R.drawable.ic_ups_disconnected,null)

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
                    Toast.makeText(this, "Press Again to Disconnect UPS", Toast.LENGTH_SHORT).show()
                }else
                    super.onBackPressed()
            }
        }
    }

    /**
     * Switch the current fragment displayed with another
     *
     * @param fragClass
     */
    private fun changeFragment(fragClass: KClass<out Fragment>, backstab:Boolean=true){

        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()

        val frag = supportFragmentManager.findFragmentByTag(fragClass.simpleName)
        if(frag==null)
            transaction.replace(R.id.fragment_container, fragClass.java, null, fragClass.simpleName)
        else
            transaction.replace(R.id.fragment_container, frag, fragClass.simpleName)

        if(backstab)
            transaction.addToBackStack(null);
        transaction.commit()



    }
    private fun goBackToUpsSelector(){
        Intent(this, StartingActivity::class.java).apply{
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP  or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }.also { it1: Intent ->
            startActivity(it1)
        }
    }



    val alert by lazy {
        AlertDialog.Builder(this)
            .setTitle("Connessione interrotta")
            .setMessage("La connessione con l'UPS sì è interrotta come procedere?")
            .setPositiveButton("Esci") { dialogInterface, i ->  goBackToUpsSelector()}
            .setNegativeButton("Attendi") { dialogInterface, i -> }
            .create()
    }
    private lateinit var mService: UpsDataFetcherService
    private var lastConnState = UpsDataFetcherService.ConnectionState.CONNECTED
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            mService= (service as UpsDataFetcherService.LocalBinder).getService()
            mService.connectionStateBus.events.observe(this@MainActivity){
                //Act only if the state change
                if(it != lastConnState){
                    //Change the connection icon
                    toolbar.menu.findItem(R.id.ups_conn_status).icon = when(it){
                        UpsDataFetcherService.ConnectionState.DISCONNECTED -> ResourcesCompat.getDrawable(resources,R.drawable.ic_ups_disconnected,null)
                        UpsDataFetcherService.ConnectionState.CONNECTED -> ResourcesCompat.getDrawable(resources,R.drawable.ic_ups_connected,null)
                    }
                    //Change the disconnected alert
                    if(it==UpsDataFetcherService.ConnectionState.DISCONNECTED)
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
        //When the app go in background the service is stopped
        stopService(Intent(this,UpsDataFetcherService::class.java))
    }

    override fun onStart() {
        super.onStart()


        //When the MainActivity is created (or the app resumed) the service is (re)started and binded
        Intent(this, UpsDataFetcherService::class.java).apply {
            putExtra("ip",intent.getStringExtra("ip")!!)
            putExtra("port",intent.getIntExtra("port",0))
            putExtra("interval",1000L)
        }.also {
            startService(it)
        }

        bindService(Intent(this, UpsDataFetcherService::class.java),connection,0)


    }

}