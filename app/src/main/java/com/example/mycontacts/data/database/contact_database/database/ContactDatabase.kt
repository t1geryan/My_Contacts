package com.example.mycontacts.data.database.contact_database.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mycontacts.data.database.contact_database.dao.ContactDao
import com.example.mycontacts.data.database.contact_database.entities.ContactEntity

@Database(
    version = 1,
    entities = [
        ContactEntity::class
    ]
)
abstract class ContactDatabase : RoomDatabase() {

    abstract fun getContactDao() : ContactDao

}