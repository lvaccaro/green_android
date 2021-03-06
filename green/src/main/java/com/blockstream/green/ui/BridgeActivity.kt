package com.blockstream.green.ui

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.blockstream.green.R
import com.blockstream.green.databinding.MainActivityBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BridgeActivity : AppCompatActivity(), IActivity {

    private lateinit var navController: NavController
    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MainActivityBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        supportActionBar?.also {
            // Prevent replacing title from NavController
            it.setDisplayShowTitleEnabled(false)
        }


        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        val inflater = navController.navInflater

        val isPin = PIN == intent.action
        val isBackupRecovery = BACKUP_RECOVERY == intent.action

        val graph = when {
            isPin -> {
                inflater.inflate(R.navigation.settings_nav_graph)
            }
            isBackupRecovery -> {
                inflater.inflate(R.navigation.nav_graph).also {
                    it.startDestination = R.id.recoveryIntroFragment
                }
            }
            else -> {
                inflater.inflate(
                    R.navigation.nav_graph
                )
            }
        }

        if(isPin){
            navController.setGraph(graph, intent.extras)
        } else if(isBackupRecovery){
            navController.setGraph(graph, intent.extras)
        } else{
            navController.graph = graph
        }

        val appBarConfiguration = AppBarConfiguration(
            setOf()
        )

        binding.toolbar.setupWithNavController(navController, appBarConfiguration)

        binding.toolbar.setNavigationOnClickListener {
            if(!NavigationUI.navigateUp(
                navController,
                appBarConfiguration
            )) {
                finish()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            android.R.id.home ->
                if (navController.previousBackStackEntry == null) {
                    finish()
                }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun isDrawerOpen() = false

    override fun closeDrawer() { }

    override fun lockDrawer(isLocked: Boolean) { }

    override fun setToolbarVisibility(isVisible: Boolean){
        binding.appBarLayout.isVisible = isVisible
    }

    override fun setToolbar(
        title: String?,
        subtitle: String?,
        drawable: Drawable?,
        button: CharSequence?,
        buttonListener: View.OnClickListener?
    ){
        binding.toolbar.set(title, subtitle, drawable, button, buttonListener)
    }

    override fun setTitle(title: CharSequence?) {
        setToolbar(title?.toString() ?: "")
    }

    companion object{
        const val PIN = "PIN"
        const val BACKUP_RECOVERY = "BACKUP_RECOVERY"
    }
}