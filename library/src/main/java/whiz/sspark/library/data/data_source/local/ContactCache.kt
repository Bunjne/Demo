package whiz.sspark.library.data.dataSource.local

import whiz.sspark.library.data.entity.Contact
import java.util.*

interface ContactCache {
    fun saveContacts(contacts: List<Contact>)
    fun getContacts(): List<Contact>?
    fun getLatestDateTime(): Date
}