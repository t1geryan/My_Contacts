package com.example.mycontacts.di

import com.example.mycontacts.model.ContactListLocalService
import com.example.mycontacts.model.ContactListRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ModelModule {

    @Binds
    abstract fun bindsContactListRepository(
        contactListLocalService: ContactListLocalService
    ) : ContactListRepository
}