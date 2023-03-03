package com.example.mycontacts

import android.app.Application
import com.example.mycontacts.model.ContactListLocalService

class App : Application() {

    val contactListLocalService = ContactListLocalService()
}