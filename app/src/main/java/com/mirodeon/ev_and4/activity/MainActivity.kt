package com.mirodeon.ev_and4.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.mirodeon.ev_and4.R
import com.mirodeon.ev_and4.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    var binding: ActivityMainBinding? = null
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        setNavigation()
        changeOnDestination()
        setNavigationToolBar()
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }

    private fun setNavigationToolBar() {
        binding?.toolbarMenu?.imageAddAction?.setOnClickListener {
            navController.navigate(R.id.addExpenseFragment)
        }
    }

    private fun setNavigation() {
        navController = findNavController(R.id.navHostFragment)
        setSupportActionBar(binding?.toolbarMenu?.root)
        NavigationUI.setupActionBarWithNavController(
            this, navController, AppBarConfiguration(
                setOf(
                    R.id.expenseListFragment
                )
            )
        )
    }

    private fun changeOnDestination() {
        val toolbarTitle = binding?.toolbarMenu?.titleToolbar
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.expenseListFragment -> {
                    toolbarTitle?.text = getString(R.string.list)
                    binding?.toolbarMenu?.root?.visibility = View.VISIBLE
                }

                R.id.addExpenseFragment -> {
                    toolbarTitle?.text = getString(R.string.add)
                    binding?.toolbarMenu?.root?.visibility = View.VISIBLE
                }

                else -> {
                    toolbarTitle?.text = getString(R.string.no_title)
                    binding?.toolbarMenu?.root?.visibility = View.INVISIBLE
                }
            }
            visibilityActionBarBtn(destination.id)
        }
    }

    private fun visibilityActionBarBtn(id: Int) {
        binding?.toolbarMenu?.imageAddAction?.visibility =
            if (id == R.id.expenseListFragment) View.VISIBLE else View.INVISIBLE
        binding?.toolbarMenu?.imageSaveAction?.visibility =
            if (id == R.id.addExpenseFragment) View.VISIBLE else View.INVISIBLE
    }
}