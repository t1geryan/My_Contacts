package com.example.mycontacts.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mycontacts.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }

        if (savedInstanceState == null)
            supportFragmentManager
                .beginTransaction()
                .add(binding.fragmentContainer.id, ContactListFragment())
                .commit()
    }


}