package com.example.contactsapp.network

import com.example.contactsapp.data.ContactsData
import com.example.contactsapp.database.Item
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkContactsContainer(val data: List<NetworkContacts>)

@JsonClass(generateAdapter = true)
data class NetworkContacts(
    val firstName: String,
    val id: String,
    val lastName: String,
    val picture: String,
    val title: String,
    var isFavorite: Boolean = false
)

fun NetworkContactsContainer.asDomainModel(): List<ContactsData> {
    return data.map {
        ContactsData(
            firstName = it.firstName,
            id = it.id,
            lastName = it.lastName,
            picture = it.picture,
            title = it.title,
            isFavorite = it.isFavorite)
    }
}

fun NetworkContactsContainer.asDatabaseModel(): List<Item> {
    return data.map {
        Item(
            firstName = it.firstName,
            id = it.id,
            lastName = it.lastName,
            picture = it.picture,
            title = it.title,
            isFavorite = it.isFavorite
        )
    }
}