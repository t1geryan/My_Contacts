package com.example.mycontacts.di

import android.content.Context
import androidx.room.Room
import com.example.mycontacts.data.database.contact_database.dao.ContactDao
import com.example.mycontacts.data.database.contact_database.database.ContactDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ContactDatabaseModule {

    @Provides
    @Singleton
    fun provideContactDatabase(@ApplicationContext appContext: Context): ContactDatabase =
        Room.databaseBuilder(appContext, ContactDatabase::class.java, "contacts.db").build()

    @Provides
    @Singleton
    fun provideContactDao(contactDatabase: ContactDatabase): ContactDao =
        contactDatabase.getContactDao()
}