package com.example.mycontacts.data.repostory

import com.example.mycontacts.data.content_provider.contact_provider.dao.ContactProviderDao
import com.example.mycontacts.data.database.contact_database.dao.ContactDao
import com.example.mycontacts.domain.mapper.ContactMapper
import com.example.mycontacts.domain.model.Contact
import com.example.mycontacts.domain.repository.ContactListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

// TODO : implement ContactListRepository members
@Singleton
class ContactListRepositoryImpl @Inject constructor(
    private val contactMapper: ContactMapper,
    private val contactDao: ContactDao,
    private val contactProviderDao: ContactProviderDao
) : ContactListRepository {

    override suspend fun getAllContacts(): Flow<List<Contact>> = contactDao.getAllContacts().map {
        it.map { contactEntity ->
            contactMapper.entityToDomain(contactEntity)
        }
    }

    override suspend fun getFavoriteContacts(): Flow<List<Contact>> =
        contactDao.getFavoriteContacts().map {
            it.map { contactEntity ->
                contactMapper.entityToDomain(contactEntity)
            }
        }

    override suspend fun addContact(contact: Contact) {
        val contactEntity = contactMapper.domainToEntity(contact)
        contactDao.addContact(contactEntity)
    }

    override suspend fun deleteContact(contact: Contact) {
        val contactEntity = contactMapper.domainToEntity(contact)
        contactDao.deleteContactById(contactEntity.id)
    }

    override suspend fun deleteAllContacts() {
        contactDao.deleteAllContacts()
    }

    override suspend fun changeContactFavoriteStatus(contact: Contact) {
        val newContact = contact.copy(isFavorite = !contact.isFavorite)
        val contactEntity = contactMapper.domainToEntity(newContact)
        contactDao.updateContact(contactEntity)
    }

    override suspend fun changeContactData(newContact: Contact) {
        val contactEntity = contactMapper.domainToEntity(newContact)
        contactDao.updateContact(contactEntity)
    }

    override suspend fun syncContacts() {
        val contactList = contactProviderDao.getAllContacts()
        contactList.forEach {
            contactDao.addContact(it.toContactEntity())
        }
    }
}
