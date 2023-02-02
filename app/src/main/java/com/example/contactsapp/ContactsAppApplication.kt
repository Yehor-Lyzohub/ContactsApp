package com.example.contactsapp

import android.app.Application
import com.example.contactsapp.database.ItemRoomDatabase

class ContactsAppApplication : Application() {
    val database: ItemRoomDatabase by lazy { ItemRoomDatabase.getDatabase(this) }
}