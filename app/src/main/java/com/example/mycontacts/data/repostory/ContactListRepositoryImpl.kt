package com.example.mycontacts.data.repostory

import com.example.mycontacts.data.database.contact_database.dao.ContactDao
import com.example.mycontacts.domain.mapper.ContactMapper
import com.example.mycontacts.domain.model.Contact
import com.example.mycontacts.domain.repository.ContactListRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

// TODO : implement ContactListRepository members
@Singleton
class ContactListRepositoryImpl @Inject constructor(
    private val contactMapper: ContactMapper,
    private val contactDao: ContactDao,
) : ContactListRepository {
    override fun getContacts(): Flow<List<Contact>> {
        TODO("Not yet implemented: get all contacts from DB and make from Array<ContactEntity> Flow<List<Contact>>")
    }

    override fun getFavoriteContacts(): Flow<List<Contact>> {
        TODO("Not yet implemented: get all favorite contacts from DB and make from Array<ContactEntity> Flow<List<Contact>>")
    }

    override fun addContact(contact: Contact) {
        TODO("Not yet implemented : add contact to DB")
    }

    override fun deleteContact(contact: Contact) {
        TODO("Not yet implemented : delete contact from DB")
    }

    override fun changeContactFavoriteStatus(contact: Contact) {
        TODO("Not yet implemented : change contact favorite status")
    }

    override fun changeContactData(oldContact: Contact, newContact: Contact) {
        TODO("Not yet implemented: change contact data")
    }

}
