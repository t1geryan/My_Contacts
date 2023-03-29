package com.example.mycontacts.ui.main_activity

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.provider.Settings
import android.util.TypedValue
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.mycontacts.R
import com.example.mycontacts.databinding.ActivityMainBinding
import com.example.mycontacts.domain.model.Contact
import com.example.mycontacts.ui.contract.*
import com.example.mycontacts.ui.tabs_screen.TabsFragment
import com.example.mycontacts.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), SideEffectsApi, FragmentResultApi {

    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var preferences: SharedPreferences

    private var navController: NavController? = null

    private val fragmentCreateListener = object  : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
            super.onFragmentViewCreated(fm, f, v, savedInstanceState)
            if (f is NavHostFragment || f is TabsFragment) return
            changeNavController(f.findNavController())
            updateUI(f)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }

        val navHost = supportFragmentManager.findFragmentById(R.id.rootFragmentContainer) as NavHostFragment
        val navController = navHost.navController
        prepareNavController(navController)

        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentCreateListener, true)
    }

    override fun onDestroy() {
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentCreateListener)
        navController = null
        super.onDestroy()
    }

    // Permissions

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            CALL_PERMISSION_REQUEST_CODE -> {
                if (grantResults[0] == PackageManager.PERMISSION_DENIED
                    && !ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE))
                    if (preferences.getBoolean(SHOULD_REQUEST_CALL_PERMISSION_PREF, true))
                        askUserToOpenAppSettings(SHOULD_REQUEST_CALL_PERMISSION_PREF)
                    else
                        showToast(resources.getString(R.string.no_call_permission))
            }
        }
    }

    private fun askUserToOpenAppSettings(shouldRequestPreferenceKey: String) {
        val appSettingsIntent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", packageName, null)
        )
        val isSettingsExist: Boolean = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val resolveInfoFlags = PackageManager.ResolveInfoFlags.of(PackageManager.MATCH_DEFAULT_ONLY.toLong())
            packageManager.resolveActivity(appSettingsIntent, resolveInfoFlags ) != null
        } else
            @Suppress("DEPRECATION")
            packageManager.resolveActivity(appSettingsIntent, PackageManager.MATCH_DEFAULT_ONLY) != null

        if (isSettingsExist) {
            val listener = DialogInterface.OnClickListener { _, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> startActivity(appSettingsIntent)
                    DialogInterface.BUTTON_NEUTRAL -> preferences.edit().putBoolean(shouldRequestPreferenceKey, false).apply()
                }
            }

            AlertDialog.Builder(this)
                .setTitle(resources.getString(R.string.denied_permission_dialog_title))
                .setMessage(resources.getString(R.string.give_permissions_message, resources.getString(R.string.make_calls)))
                .setPositiveButton(resources.getString(R.string.open_settings), listener)
                .setNegativeButton(resources.getString(R.string.close_dialog), null)
                .setNeutralButton(resources.getString(R.string.dont_ask_again), listener)
                .create()
                .show()

        }
    }

    // Nav Component

    private fun prepareNavController(navController: NavController) {
        val isFirstLaunch = preferences.getBoolean(Constants.FIRST_LAUNCH_KEY, true)

        val graph = navController.navInflater.inflate(R.navigation.root_graph)
        graph.setStartDestination(
            if (isFirstLaunch) {
                preferences.edit().putBoolean(Constants.FIRST_LAUNCH_KEY, false).apply()
                R.id.onBoardingFragment
             } else
                 R.id.tabsFragment
        )
        navController.graph = graph

    }

    private fun changeNavController(newNavController: NavController) {
        if (navController == newNavController) return
        navController = newNavController
    }

    //  SideEffectsApi implementation
    override fun requestPermission(permList: Array<String>, requestCode: Int) {
        ActivityCompat.requestPermissions(this, permList, requestCode)
    }

    override fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun startCall(contact: Contact) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
            == PackageManager.PERMISSION_GRANTED) {
            try {
                startActivity(Intent(Intent.ACTION_CALL, Uri.parse("tel: ${contact.number}")))
            } catch (e: Exception) {
                showToast(resources.getString(R.string.cant_call))
            }
        } else
            requestPermission(arrayOf(Manifest.permission.CALL_PHONE), CALL_PERMISSION_REQUEST_CODE)
    }

    // FragmentResultApi implementation
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

    // UI

    private fun updateUI(fragment : Fragment) {
        binding.materialToolbar.title = navController?.currentDestination?.label ?: getString(R.string.app_name)

        binding.materialToolbar.menu.clear()
        if (fragment is HasCustomActionToolbar)
            createCustomToolbarAction(fragment.getCustomAction())
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

        private const val CALL_PERMISSION_REQUEST_CODE = 0
        private const val SHOULD_REQUEST_CALL_PERMISSION_PREF = "CALL_PERM_PREf"
    }

}