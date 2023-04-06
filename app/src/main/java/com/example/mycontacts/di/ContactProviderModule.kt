package com.example.mycontacts.di

import com.example.mycontacts.data.content_provider.contact_provider.dao.ContactProviderDao
import com.example.mycontacts.data.content_provider.contact_provider.provider.ContactProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ContactProviderModule {

    @Binds
    abstract fun bindContactProvider(
        contactProvider: ContactProvider
    ): ContactProviderDao
}