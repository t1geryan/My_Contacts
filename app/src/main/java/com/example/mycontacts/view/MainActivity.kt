package com.example.mycontacts.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mycontacts.R
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