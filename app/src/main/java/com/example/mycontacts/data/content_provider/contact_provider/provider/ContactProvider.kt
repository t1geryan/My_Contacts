package com.example.mycontacts.data.content_provider.contact_provider.provider

import android.content.Context
import android.provider.ContactsContract
import com.example.mycontacts.data.content_provider.contact_provider.dao.ContactProviderDao
import com.example.mycontacts.data.content_provider.contact_provider.entities.ContactProviderEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ContactProvider @Inject constructor(
    @ApplicationContext private val context: Context,
) : ContactProviderDao {

    override suspend fun getAllContacts(): List<ContactProviderEntity> {
        val result = mutableListOf<ContactProviderEntity>()
        val contentResolver = context.contentResolver
        contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)
            ?.use { cur ->
                while (cur.moveToNext()) {
                    val name =
                        cur.getString(cur.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY))
                    val id = cur.getString(cur.getColumnIndexOrThrow(ContactsContract.Contacts._ID))
                    val photoUri =
                        cur.getString(cur.getColumnIndexOrThrow(ContactsContract.Contacts.PHOTO_URI))

                    if (cur.getInt(cur.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                        contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            arrayOf(id),
                            null
                        )?.use { pCur ->
                            while (pCur.moveToNext()) {
                                val phoneNumber =
                                    pCur.getString(pCur.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
                                result.add(ContactProviderEntity(name, phoneNumber, photoUri))
                            }
                        }
                    }
                }
            }
        return result
    }
}