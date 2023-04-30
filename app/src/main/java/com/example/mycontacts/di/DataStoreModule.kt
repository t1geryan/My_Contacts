package com.example.mycontacts.di

import com.example.mycontacts.data.pref_datastore.profile_datastore.dao.PrefDataStoreDao
import com.example.mycontacts.data.pref_datastore.profile_datastore.datastore.PrefDataStore
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataStoreModule {

    @Binds
    abstract fun bindsPrefDataStore(
        prefDataStore: PrefDataStore
    ) : PrefDataStoreDao
}