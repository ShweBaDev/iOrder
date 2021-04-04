package com.galaxysoftware.istockenterpriseiorder.views

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.galaxysoftware.istockenterpriseiorder.BaseActivity
import com.galaxysoftware.istockenterpriseiorder.R
import com.galaxysoftware.istockenterpriseiorder.models.UsrCode
import com.galaxysoftware.istockenterpriseiorder.views.ui.gallery.GalleryFragment
import com.galaxysoftware.istockenterpriseiorder.views.ui.items.ItemFragment
import com.galaxysoftware.istockenterpriseiorder.views.ui.slideshow.SlideshowFragment
import com.google.android.material.navigation.NavigationView
import androidx.lifecycle.Observer
import com.galaxysoftware.istockenterpriseiorder.views.ui.checkout.CheckoutActivity
import com.galaxysoftware.istockenterpriseiorder.views.ui.outstand.OutstandFragment


class MainConsoleActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var appBarConfiguration: AppCompatActivity

    lateinit var contentView: FrameLayout

     var selectedlist: MutableList<UsrCode> = mutableListOf()

     lateinit var shoppingitem : TextView

    lateinit var imvShoppingCart : ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_console)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        shoppingitem = toolbar.findViewById(R.id.shoppingItemCount)
        imvShoppingCart = toolbar.findViewById(R.id.shoppingCart)

        setSupportActionBar(toolbar)

        imvShoppingCart.setOnClickListener{
            checkOut()
        }

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        contentView = findViewById(R.id.content_fragment)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this);

        /*For Default First Item Is Selected*/
        navView.menu.getItem(0).isChecked = true
        onNavigationItemSelected(navView.menu.getItem(0))

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
       // menuInflater.inflate(R.menu.main_console, menu)
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        var fragment : Fragment = ItemFragment()

        when (item.itemId)
        {
            R.id.nav_order-> {

                fragment = ItemFragment()
                (fragment as ItemFragment).setFunction{
                   getCardItemCount()
                }

            }
            R.id.nav_gallery ->{
                fragment = GalleryFragment()
            }
            R.id.nav_slideshow -> {
                fragment = SlideshowFragment()
            }
            R.id.nav_outstand-> {
                fragment=OutstandFragment()
            }

        }

        val fragmentManager: FragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().replace(contentView.id, fragment).commit()

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)

        return  true
    }

    override fun onBackPressed() {
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun getCardItemCount(){

        sqlUtilities.getCartItemCount()?.observe(this,Observer {
            shoppingitem.text = it.toString()
        })
    }

    private fun checkOut() {
        val intent = Intent(this, CheckoutActivity::class.java)
        startActivity(intent)
    }
}