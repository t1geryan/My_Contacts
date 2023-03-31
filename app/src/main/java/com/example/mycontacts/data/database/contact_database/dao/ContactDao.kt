package com.example.mycontacts.data.database.contact_database.dao

import androidx.room.*
import com.example.mycontacts.data.database.contact_database.entities.ContactEntity

@Dao
interface ContactDao {

    @Query("SELECT * FROM contacts")
    suspend fun getAllContacts() : Array<ContactEntity>

    @Query("SELECT * FROM contacts WHERE is_favorite = 1")
    suspend fun getFavoriteContacts() : Array<ContactEntity>

    @Query("Select * From contacts WHERE number = :number")
    suspend fun getContactByNumber(number: String) : ContactEntity

    @Query("DELETE FROM contacts WHERE number = :number")
    suspend fun deleteContactByNumber(number: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addContact(contactEntity: ContactEntity)

    @Update
    suspend fun updateContact(contactEntity: ContactEntity)

}