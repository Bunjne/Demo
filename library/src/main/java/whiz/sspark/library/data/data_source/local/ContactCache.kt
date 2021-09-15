package whiz.sspark.library.data.data_source.local

import whiz.sspark.library.data.entity.Contact
import java.util.*

interface ContactCache {
    fun saveContacts(contacts: List<Contact>)
    fun getContacts(): List<Contact>?
    fun getLatestDateTime(): Date
}