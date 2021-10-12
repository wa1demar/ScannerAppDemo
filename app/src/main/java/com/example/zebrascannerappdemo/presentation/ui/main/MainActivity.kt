package com.example.zebrascannerappdemo.presentation.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.zebrascannerappdemo.R
import com.example.zebrascannerappdemo.databinding.ActivityMainBinding
import com.example.zebrascannerappdemo.presentation.ui.base.OnBackPressedListener
import com.example.zebrascannerappdemo.presentation.utils.NetworkStateHandler
import com.example.zebrascannerappdemo.presentation.utils.currentNavigationFragment
import com.example.zebrascannerappdemo.presentation.utils.dismissBlockingAlert
import com.example.zebrascannerappdemo.presentation.utils.showBlockingError
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private var _navController: NavController? = null
    private val navController get() = _navController!!

    @Inject
    lateinit var networkStateHandler: NetworkStateHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        networkStateHandler.addNetworkStatusCallback(networkStatusCallback)
        networkStateHandler.registerNetworkReceiver()

        setupNavController()
        setupViews()
    }

    private fun setupNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        _navController = navHostFragment.findNavController()
    }

    private fun setupViews() {
        with(binding) {
            setupToolbar(toolbarContainer.toolbar)
        }
    }

    private fun setupToolbar(toolbar: Toolbar) {
        val appBarConfiguration = AppBarConfiguration(
            topLevelDestinationIds = setOf(
                R.id.homeFragment
            ),
            fallbackOnNavigateUpListener = ::onSupportNavigateUp
        )

        setSupportActionBar(toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onDestroy() {
        networkStateHandler.removeNetworkStatusCallback(networkStatusCallback)
        networkStateHandler.unregisterNetworkReceiver()
        super.onDestroy()
        _navController = null
        _binding = null
    }

    override fun onSupportNavigateUp(): Boolean {
        val currentFragment = supportFragmentManager.currentNavigationFragment
        if (currentFragment is OnBackPressedListener) {
            if (currentFragment.onBackPressed()) {
                return false
            }
        }
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private val networkStatusCallback = object : NetworkStateHandler.NetworkStatusCallback {
        override fun onAvailable() {
            dismissBlockingAlert()
        }

        override fun onLost() {
            showBlockingError(
                R.string.error_no_internet_connection,
                R.string.msg_no_internet_connection
            )
        }
    }
}