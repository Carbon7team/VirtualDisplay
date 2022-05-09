package com.carbon7.virtualdisplay

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.FragmentTransaction
import com.carbon7.virtualdisplay.databinding.ActivityMainBinding
import com.carbon7.virtualdisplay.databinding.ActivityStartingBinding
import com.carbon7.virtualdisplay.ui.ups_selector.UpsSelectorFragment

class StartingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStartingBinding
    private lateinit var toolbar: Toolbar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartingBinding.inflate(layoutInflater)
        setContentView(binding.root)



        toolbar = binding.appToolbar.toolbar
        setSupportActionBar(toolbar)




        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, UpsSelectorFragment::class.java, null)
        //transaction.addToBackStack(null);
        transaction.commit()




    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when(item.itemId){
        R.id.support -> {
            Toast.makeText(this,"ASSISTENZA", Toast.LENGTH_SHORT).show()
            true
        }
        R.id.settings -> {
            Toast.makeText(this,"IMPOSTAZIONI", Toast.LENGTH_SHORT).show()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)

        }
    }
}