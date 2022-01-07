package com.rrat.mvvm2demo.view.activites

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.work.*
import com.rrat.mvvm2demo.R
import com.rrat.mvvm2demo.databinding.ActivityMainBinding
import com.rrat.mvvm2demo.model.notification.NotifyWorker
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding

    private lateinit var mNavController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        val navView: BottomNavigationView = mBinding.navView

        mNavController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_all_dishes, R.id.navigation_favorite_dishes, R.id.navigation_random_dish
        ))

        setupActionBarWithNavController(mNavController, appBarConfiguration)
        navView.setupWithNavController(mNavController)

        startWork() 
    }


    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(mNavController, null)
    }

    fun hideBottomNavigationView()
    {
        mBinding.navView.clearAnimation()
        mBinding.navView.animate().translationY(mBinding.navView.height.toFloat()).duration = 300
        mBinding.navView.visibility = View.GONE

    }
    fun showBottomNavigationView()
    {
        mBinding.navView.clearAnimation()
        mBinding.navView.animate().translationY(0f).duration = 300
        mBinding.navView.visibility = View.VISIBLE
    }

    private fun createConstrains() = Constraints.Builder()
        .setRequiresCharging(false)
        .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
        .setRequiresBatteryNotLow(true)
        .build()

    private fun createWorkRequest() =
        PeriodicWorkRequestBuilder<NotifyWorker>(15, TimeUnit.MINUTES)
            .setConstraints(createConstrains())
            .build()

    private fun startWork(){
        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork("Periodic Work Request",
                ExistingPeriodicWorkPolicy.KEEP,
                createWorkRequest())
    }
}