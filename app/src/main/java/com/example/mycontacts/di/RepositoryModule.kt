package com.example.mycontacts.di

import com.example.mycontacts.data.repostory.ContactListRepositoryImpl
import com.example.mycontacts.domain.repository.ContactListRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindsContactListRepository(
        contactListLocalService: ContactListRepositoryImpl
    ): ContactListRepository
}