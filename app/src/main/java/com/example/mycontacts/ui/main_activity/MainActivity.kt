package com.example.mycontacts.ui.main_activity

import android.content.SharedPreferences
import android.os.Bundle
import android.util.TypedValue
import android.view.MenuItem
import android.view.View
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.mycontacts.R
import com.example.mycontacts.databinding.ActivityMainBinding
import com.example.mycontacts.ui.contact_list_screen.ContactListFragment
import com.example.mycontacts.ui.details.Action
import com.example.mycontacts.ui.details.HasCustomActionToolbar
import com.example.mycontacts.ui.details.HasCustomTitleToolbar
import com.example.mycontacts.ui.onboarding_screen.OnBoardingFragment
import com.example.mycontacts.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var preferences: SharedPreferences

    private val currentFragment : Fragment
        get() = supportFragmentManager.findFragmentById(R.id.fragmentContainer)!!

    private val fragmentCreateListener = object  : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
            super.onFragmentViewCreated(fm, f, v, savedInstanceState)
            updateToolbarUI()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }

        preferences = getSharedPreferences(Constants.APP_PREFERENCES, MODE_PRIVATE)

        if (savedInstanceState == null)
            launchFirstFragment()

        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentCreateListener, false)
    }
    override fun onDestroy() {
        super.onDestroy()
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentCreateListener)
    }

    private fun launchFirstFragment() {
        val isFirstLaunch = preferences.getBoolean(Constants.FIRST_LAUNCH_KEY, true)

        val transaction = supportFragmentManager.beginTransaction()

        if (isFirstLaunch) {
            transaction.add(binding.fragmentContainer.id, OnBoardingFragment())
            preferences.edit().putBoolean(Constants.FIRST_LAUNCH_KEY, false).apply()
        } else
            transaction.add(binding.fragmentContainer.id, ContactListFragment())

        transaction.commit()
    }

    private fun updateToolbarUI() {
        when(val fragment = currentFragment) {
            is HasCustomActionToolbar -> createCustomToolbarAction(fragment.getCustomAction())
            else -> binding.materialToolbar.menu.clear()
        }

        when (val fragment = currentFragment) {
            is HasCustomTitleToolbar -> binding.materialToolbar.setTitle(fragment.getTitle())
        }
    }

    private fun createCustomToolbarAction(action: Action) {
        val iconDrawable = DrawableCompat.wrap(ContextCompat.getDrawable(this, action.icon) ?: return)

        val typedValue = TypedValue()
        theme.resolveAttribute(com.google.android.material.R.attr.colorOnPrimary, typedValue, true)
        @ColorInt val color = typedValue.data
        iconDrawable.setTint(color)

        val menuItem = binding.materialToolbar.menu.add(action.title)
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        menuItem.icon = iconDrawable
        menuItem.setOnMenuItemClickListener {
            action.onAction.run()
            true
        }
    }
}