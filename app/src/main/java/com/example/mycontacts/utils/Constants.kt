package com.example.mycontacts.utils

object Constants {
    object SharedPreferences {
        const val NAME = "APP_PREFERENCES"
        const val FIRST_LAUNCH_PREF = "FIRST_LAUNCH_PREF"
        const val SHOULD_REQUEST_CALL_PERMISSION_PREF = "CALL_PERM_PREF"
        const val SHOULD_REQUEST_READ_CONTACTS_PERMISSION_PREF = "READ_CONTACTS_PERM_PREF"
    }
    object PrefDataStore {
        const val NAME = "PROFILE_PREFERENCES"
    }

    object RecyclerViewUtils {
        const val CONTACTS_COLUMN_WIDTH = 136F
    }
}