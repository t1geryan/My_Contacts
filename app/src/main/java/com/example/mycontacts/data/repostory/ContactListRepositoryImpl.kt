package com.example.mycontacts.data.repostory

import com.example.mycontacts.data.content_provider.contact_provider.dao.ContactProviderDao
import com.example.mycontacts.data.database.contact_database.dao.ContactDao
import com.example.mycontacts.domain.mapper.ContactMapper
import com.example.mycontacts.domain.model.Contact
import com.example.mycontacts.domain.model.Result
import com.example.mycontacts.domain.repository.ContactListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactListRepositoryImpl @Inject constructor(
    private val contactMapper: ContactMapper,
    private val contactDao: ContactDao,
    private val contactProviderDao: ContactProviderDao
) : ContactListRepository {

    /*  TODO #2 Если не получился TODO #1
        Требуется рефакторинг: добавить Result.Error и Result.Loading
        Нужно сделать это так, чтобы метод возвращал тот же Flow что получает из Dao
        Таким образом будет организована синхронизация изменений списка
     */
    override suspend fun getAllContacts(onlyFavorites: Boolean): Flow<Result<List<Contact>>> =
        if (onlyFavorites) {
            contactDao.getFavoriteContacts()
        } else {
            contactDao.getAllContacts()
        }.map {
            if (it.isEmpty()) {
                Result.EmptyOrNull()
            } else {
                Result.Success(it.map { cE ->
                    contactMapper.entityToDomain(cE)
                })
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
