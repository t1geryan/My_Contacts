package com.example.mycontacts.data.database.contact_database.dao

import androidx.room.*
import com.example.mycontacts.data.database.contact_database.entities.ContactEntity

@Dao
interface ContactDao {

    @Query("SELECT * FROM contacts")
    suspend fun getAll() : List<ContactEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addContact(contactEntity: ContactEntity)

    @Query("DELETE FROM contacts WHERE number = :number")
    suspend fun deleteContact(number: String)

    @Update
    suspend fun updateContact(contactEntity: ContactEntity)

}