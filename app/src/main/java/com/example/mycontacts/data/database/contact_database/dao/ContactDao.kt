package com.example.mycontacts.data.database.contact_database.dao

import androidx.room.*
import com.example.mycontacts.data.database.contact_database.entities.ContactEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {

    @Query("SELECT * FROM contacts")
    fun getAllContacts(): Flow<List<ContactEntity>>

    @Query("SELECT * FROM contacts WHERE is_favorite = 1")
    fun getFavoriteContacts(): Flow<List<ContactEntity>>

    @Query("DELETE FROM contacts WHERE id = :id")
    suspend fun deleteContactById(id: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addContact(contactEntity: ContactEntity)

    @Update
    suspend fun updateContact(contactEntity: ContactEntity)

}