package com.example.contactsapp.repository

import com.example.contactsapp.ContactsListApi
import com.example.contactsapp.data.ContactsData
import com.example.contactsapp.database.Item
import com.example.contactsapp.database.ItemRoomDatabase
import com.example.contactsapp.database.asDomainModel
import com.example.contactsapp.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class ContactsRepository(private val database: ItemRoomDatabase) {
    val contacts: Flow<List<ContactsData>> = database.itemDao.getItems().map {
        it.asDomainModel()
    }

    suspend fun refreshContacts() {
        withContext(Dispatchers.IO) {
            val data = ContactsListApi.retrofitService.getData()
            database.itemDao.insertAll(data.asDatabaseModel())
        }
    }

    suspend fun addOrRemoveFavorite(id: String) {
        withContext(Dispatchers.IO) {
            val contact = retrieveItem(id)
            val newContact = contact.copy(isFavorite = !contact.isFavorite)

            database.itemDao.update(newContact)
        }
    }

    private fun retrieveItem(id: String): Item {
        return database.itemDao.getItem(id)
    }
}

    /*

    contacts.map {
                it.sortedWith(compareBy( {it.isFavorite}, {it.firstName})).sortedByDescending { it.isFavorite }
            }

    fun getUpdatedItemEntry(
        id: String,
        firstName: String,
        lastName: String,
        picture: String,
        title: String,
        isFavorite: Boolean
    ): Item {
        return Item(
            id = id,
            firstName = firstName,
            lastName = lastName,
            picture = picture,
            title = title,
            isFavorite = isFavorite
        )
    }

    suspend fun updateItem(item: Item) {
        //достать из дао айтем по контакт айди.
        //изменить изФейфорит, сделать апдейт со скопированным айтемом
        addOrRemoveFavorite(item.id)
        withContext(Dispatchers.IO) {
            database.itemDao.update(item)
        }
    }
    */
