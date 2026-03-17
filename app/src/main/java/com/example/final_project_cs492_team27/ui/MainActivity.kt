package com.example.final_project_cs492_team27.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.final_project_cs492_team27.R
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.fragment.NavHostFragment
import androidx.appcompat.app.ActionBarDrawerToggle

class   MainActivity : AppCompatActivity() {

    lateinit var appBarConfig: AppBarConfiguration
    //lateinit var toggle: ActionBarDrawerToggle //declaration of toggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)

        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.nav_host_fragment
        ) as NavHostFragment
        val navController = navHostFragment.navController
        //makes it so the hamburger only appears in settings and history frags
        appBarConfig = AppBarConfiguration(
            setOf(R.id.translation_history, R.id.settings),
            drawerLayout
        )

        val topAppBar: MaterialToolbar = findViewById(R.id.top_app_bar)
        setSupportActionBar(topAppBar)
        setupActionBarWithNavController(navController, appBarConfig)

        findViewById<NavigationView>(R.id.nav_view)?.setupWithNavController(navController)

        //connects hamburger icon with the drawer, also turns into a back arrow after navigation
//        toggle = ActionBarDrawerToggle(
//            this,
//            drawerLayout,
//            R.string.nav_open,
//            R.string.nav_close
//        )
//        drawerLayout.addDrawerListener(toggle)
//        toggle.syncState()

//        navController.addOnDestinationChangedListener { _, destination,_ ->
//            when (destination.id){
//                R.id.home_screen -> {
//                    supportActionBar?.hide()
//                }
//                R.id.translation_history, R.id.settings -> {
//                   supportActionBar?.show()
//                    topAppBar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
//                    topAppBar.setNavigationOnClickListener {
//                        drawerLayout.open()
//                    }
//                }
//            }
//        }
    }
    override fun onSupportNavigateUp(): Boolean {
        val navController = (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
        return navController.navigateUp() || super.onSupportNavigateUp()    }
}