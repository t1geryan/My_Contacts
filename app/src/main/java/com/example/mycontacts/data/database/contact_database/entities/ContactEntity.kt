package com.example.mycontacts.data.database.contact_database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "contacts",
    indices = [
        Index("number", unique = true)
    ]
)
data class ContactEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val name: String,
    val number: String,
    val photo: String?,
    @ColumnInfo(name = "is_favorite") val isFavorite: Boolean,
)