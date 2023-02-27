package com.example.mycontacts.model

abstract class ContactListRepository {

    protected var contactList = mutableListOf<Contact>()
    val contacts: List<Contact>
        get() = contactList

    private val listeners = mutableListOf<OnContactListChangeListener>()

    abstract fun getContact(position: Int): Contact

    abstract fun findFirst(contact: Contact): Int

    abstract fun addContact(contact: Contact)

    abstract fun deleteContact(contact: Contact)

    abstract fun changeContactFavoriteStatus(contact: Contact)

    inner class OnContactListChangeListener(private val block: (List<Contact>) -> Unit) : Runnable {
        override fun run() {
            block.invoke(contacts)
        }
    }

    fun addListener(listener: OnContactListChangeListener) {
        listeners.add(listener)
    }

    fun removeListener(listener: OnContactListChangeListener) {
        listeners.remove(listener)
    }

    protected fun notifyChanges() {
        listeners.forEach {
            it.run()
        }
    }
}