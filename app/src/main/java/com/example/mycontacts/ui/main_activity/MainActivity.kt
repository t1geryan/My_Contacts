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
import com.example.mycontacts.ui.details.HasNotBottomNavigationBar
import com.example.mycontacts.ui.favorite_contact_list_screen.FavoriteContactListFragment
import com.example.mycontacts.ui.navigation.Navigator
import com.example.mycontacts.ui.onboarding_screen.OnBoardingFragment
import com.example.mycontacts.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), Navigator {

    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var preferences: SharedPreferences

    private val currentFragment : Fragment
        get() = supportFragmentManager.findFragmentById(R.id.fragmentContainer)!!

    private val fragmentCreateListener = object  : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
            super.onFragmentViewCreated(fm, f, v, savedInstanceState)
            updateUI()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }

        if (savedInstanceState == null)
            launchFirstScreen()

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.contacts_btn -> launchContactListScreen()
                R.id.favorites_btn -> launchFavoriteContactsScreen()
                R.id.account_btn -> return@setOnItemSelectedListener false // todo add account screen
                else -> throw Exception("IllegalMenuItemException")
            }
            true
        }

        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentCreateListener, false)
    }

    override fun onDestroy() {
        super.onDestroy()
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentCreateListener)
    }

    private fun launchFirstScreen() {
        val isFirstLaunch = preferences.getBoolean(Constants.FIRST_LAUNCH_KEY, true)

        if (isFirstLaunch) {
            launchScreen(OnBoardingFragment())
            preferences.edit().putBoolean(Constants.FIRST_LAUNCH_KEY, false).apply()
        } else
            launchContactListScreen()
    }

    private fun launchScreen(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainer.id, fragment)
            .commit()
    }

    override fun launchContactListScreen() {
        launchScreen(ContactListFragment())
    }

    override fun launchFavoriteContactsScreen() {
        launchScreen(FavoriteContactListFragment())
    }


    private fun updateUI() {
        val fragment = currentFragment

        binding.materialToolbar.menu.clear()
        if (fragment is HasCustomActionToolbar) {
            createCustomToolbarAction(fragment.getCustomAction())
        }

        when (fragment) {
            is HasCustomTitleToolbar -> binding.materialToolbar.setTitle(fragment.getTitle())
            else -> binding.materialToolbar.setTitle(R.string.app_name)
        }

        binding.bottomNavigationView.visibility = when (currentFragment) {
            is HasNotBottomNavigationBar -> View.INVISIBLE
            else -> View.VISIBLE
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