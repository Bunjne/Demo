package whiz.sspark.library.data.data_source.local.impl

import st.lowlevel.storo.Storo
import whiz.sspark.library.data.data_source.local.ContactCache
import whiz.sspark.library.data.entity.Contact
import java.util.*
import java.util.concurrent.TimeUnit

class ContactCacheImpl : ContactCache {

    private val contactListKey = "ContactListKey"
    private val contactListLatestDateTimeKey = "ContactListLatestDateTimeKey"
    private val contactListExpirySecond = 7L

    override fun saveContacts(contacts: List<Contact>) {
        Storo.put(contactListKey, contacts)
            .setExpiry(contactListExpirySecond, TimeUnit.DAYS)
            .execute()

        Storo.put(contactListLatestDateTimeKey, Date())
            .execute()
    }

    override fun getContacts(): List<Contact>? {
        val hasExpired = Storo.hasExpired(contactListKey).execute() ?: true
        return if (hasExpired) {
            null
        } else {
            Storo.get(contactListKey, Array<Contact>::class.java).execute()?.toList()
        }
    }

    override fun getLatestDateTime(): Date {
        val hasExpired = Storo.hasExpired(contactListLatestDateTimeKey).execute() ?: true
        return if (hasExpired) {
            Date()
        } else {
            Storo.get(contactListLatestDateTimeKey, Date::class.java).execute() ?: Date()
        }
    }
}