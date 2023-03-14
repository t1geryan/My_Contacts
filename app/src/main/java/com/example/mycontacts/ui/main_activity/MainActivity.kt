package com.example.mycontacts.ui.main_activity

import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.TypedValue
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.LifecycleOwner
import com.example.mycontacts.R
import com.example.mycontacts.databinding.ActivityMainBinding
import com.example.mycontacts.ui.contact_list_screen.ContactListFragment
import com.example.mycontacts.ui.details.Action
import com.example.mycontacts.ui.details.HasCustomActionToolbar
import com.example.mycontacts.ui.details.HasCustomTitleToolbar
import com.example.mycontacts.ui.details.HasNotBottomNavigationBar
import com.example.mycontacts.ui.favorite_contact_list_screen.FavoriteContactListFragment
import com.example.mycontacts.ui.input_contact_screen.ContactInputDialogFragment
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

    override fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun launchContactInputScreen(name: String, number: String) {
        ContactInputDialogFragment.newInstance(name, number).show(supportFragmentManager, ContactInputDialogFragment.TAG)
    }

    override fun <T : Parcelable> publishResult(result: T) {
        supportFragmentManager.setFragmentResult(result.javaClass.name, bundleOf(KEY_RESULT to result))
    }

    override fun <T : Parcelable> listenResult(
        clazz: Class<T>,
        owner: LifecycleOwner,
        listener: (T) -> Unit ) {
        val fragmentResultListener = FragmentResultListener { _, bundle ->
            if (Build.VERSION.SDK_INT >= 33)
                listener.invoke(bundle.getParcelable(KEY_RESULT, clazz) ?: throw Exception("NoFragmentResultException"))
            else
                @Suppress("DEPRECATION")
                listener.invoke(bundle.getParcelable(KEY_RESULT) ?: throw Exception("NoFragmentResultException"))
        }
        supportFragmentManager.setFragmentResultListener(clazz.name, owner, fragmentResultListener)
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

    companion object {
        private const val KEY_RESULT = "KEY_RESULT"
    }
}