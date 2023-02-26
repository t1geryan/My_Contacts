package com.example.mycontacts.model

import com.github.javafaker.Faker
import kotlin.random.Random
import kotlin.random.nextULong

object Repository {
    class OnContactListChangeListener(private val block: (List<Contact>) -> Unit) : Runnable {
        override fun run() {
            block.invoke(contacts)
        }
    }

    private var contactList = mutableListOf<Contact>()
    val contacts: List<Contact>
        get() = contactList

    private val listeners = mutableListOf<OnContactListChangeListener>()

    private val photos = mutableListOf(
        "https://images.unsplash.com/photo-1499996860823-5214fcc65f8f?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=766&q=80",
        "https://images.unsplash.com/photo-1554151228-14d9def656e4?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8OXx8aHVtYW58ZW58MHx8MHx8&auto=format&fit=crop&w=500&q=60",
        "https://images.unsplash.com/photo-1542909168-82c3e7fdca5c?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MTB8fGh1bWFufGVufDB8fDB8fA%3D%3D&auto=format&fit=crop&w=500&q=60",
        "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MTV8fGh1bWFufGVufDB8fDB8fA%3D%3D&auto=format&fit=crop&w=500&q=60",
        "https://images.unsplash.com/photo-1601412436009-d964bd02edbc?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MTd8fGh1bWFufGVufDB8fDB8fA%3D%3D&auto=format&fit=crop&w=500&q=60",
        "https://images.unsplash.com/photo-1597223557154-721c1cecc4b0?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MjF8fGh1bWFufGVufDB8fDB8fA%3D%3D&auto=format&fit=crop&w=500&q=60",
        "https://images.unsplash.com/photo-1589571894960-20bbe2828d0a?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MjJ8fGh1bWFufGVufDB8fDB8fA%3D%3D&auto=format&fit=crop&w=500&q=60",
        "https://images.unsplash.com/photo-1544348817-5f2cf14b88c8?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8Mjh8fGh1bWFufGVufDB8fDB8fA%3D%3D&auto=format&fit=crop&w=500&q=60",
        "https://images.unsplash.com/photo-1552058544-f2b08422138a?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MzJ8fGh1bWFufGVufDB8fDB8fA%3D%3D&auto=format&fit=crop&w=500&q=60",
        "https://images.unsplash.com/photo-1560787313-5dff3307e257?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MzR8fGh1bWFufGVufDB8fDB8fA%3D%3D&auto=format&fit=crop&w=500&q=60",
        "https://images.unsplash.com/photo-1597586124394-fbd6ef244026?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MzZ8fGh1bWFufGVufDB8fDB8fA%3D%3D&auto=format&fit=crop&w=500&q=60",
        "https://images.unsplash.com/photo-1618835962148-cf177563c6c0?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8Mzh8fGh1bWFufGVufDB8fDB8fA%3D%3D&auto=format&fit=crop&w=500&q=60",
        "https://images.unsplash.com/photo-1578489758854-f134a358f08b?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8NDF8fGh1bWFufGVufDB8fDB8fA%3D%3D&auto=format&fit=crop&w=500&q=60",
        "https://images.unsplash.com/photo-1573140247632-f8fd74997d5c?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8NDR8fGh1bWFufGVufDB8fDB8fA%3D%3D&auto=format&fit=crop&w=500&q=60",
        "https://images.unsplash.com/photo-1545167622-3a6ac756afa4?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8NDd8fGh1bWFufGVufDB8fDB8fA%3D%3D&auto=format&fit=crop&w=500&q=60",
    )

    init {
        val faker = Faker.instance()
        photos.shuffle()
        contactList = (1..20).map {
            Contact(
                name = faker.name().fullName(),
                number = faker.phoneNumber().phoneNumber(),
                photo = if(Random.nextBoolean()) photos[it % photos.size] else "",
                isFavorite = Random.nextBoolean(),
                id = it.toULong()
            )
        }.toMutableList()
    }

    fun getContact(position: Int): Contact {
        return contactList[position]
    }

    fun findFirst(contact: Contact): Int {
        val index = contactList.indexOfFirst { it == contact }
        if (index != -1)
            return index
        else
            throw Exception("ContactNotExistException")
    }

    fun addContact(contact: Contact) {
        if (contact.id == 0UL)
            while (contactList.find {it.id == contact.id} != null )
                contact.id = Random.nextULong()
        else if (contactList.find { it.id == contact.id } != null)
            throw Exception("RecurringPrimaryKey")
        contactList.add(0, contact)
        notifyChanges()
    }

    fun deleteContact(contact: Contact) {
        contactList.remove(contact)
        notifyChanges()
    }

    fun changeContactFavoriteStatus(contact: Contact) {
        val index = findFirst(contact)
        contactList[index].isFavorite = !contactList[index].isFavorite
        notifyChanges()
    }

    fun addListener(listener: OnContactListChangeListener) {
        listeners.add(listener)
    }

    fun removeListener(listener: OnContactListChangeListener) {
        listeners.remove(listener)
    }

    private fun notifyChanges() {
        listeners.forEach {
            it.run()
        }
    }
}
