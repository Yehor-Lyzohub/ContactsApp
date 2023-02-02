package com.example.contactsapp.data

data class ContactsData(
    val firstName: String,
    val id: String,
    val lastName: String,
    val picture: String,
    val title: String,
    var isFavorite: Boolean = false
)