package com.example.contactsapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.contactsapp.data.ContactsData
import com.example.contactsapp.data.Location
import com.example.contactsapp.data.UserInfo

@Entity(tableName = "item")
data class Item(
    @PrimaryKey
    val id: String,
    val firstName: String,
    val lastName: String,
    val picture: String,
    val title: String,
    var isFavorite: Boolean = false
)

fun List<Item>.asDomainModel(): List<ContactsData> {
    return map {
        ContactsData(
            firstName = it.firstName,
            id = it.id,
            lastName = it.lastName,
            picture = it.picture,
            title = it.title,
            isFavorite = it.isFavorite
        )
    }
}

/*
@Entity(tableName = "details")
data class Details(
    @PrimaryKey
    val id: String,
    val dateOfBirth: String,
    val email: String,
    val firstName: String,
    val gender: String,
    val lastName: String,
    val location: Location,
    val phone: String,
    val picture: String,
    val registerDate: String,
    val title: String,
    val updatedDate: String
)

fun List<Details>.asDomainModel(): List<UserInfo> {
    return map {
        UserInfo(
            id = it.id,
            dateOfBirth = it.dateOfBirth,
            email = it.email,
            firstName = it.firstName,
            gender = it.gender,
            lastName = it.lastName,
            location = it.location,
            phone = it.phone,
            picture = it.picture,
            registerDate = it.registerDate,
            title = it.title,
            updatedDate = it.updatedDate
        )
    }
}
*/
