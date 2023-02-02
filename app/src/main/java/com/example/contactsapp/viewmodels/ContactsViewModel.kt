package com.example.contactsapp.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.example.contactsapp.UserInfoApi
import com.example.contactsapp.data.UserInfo
import com.example.contactsapp.database.Item
import com.example.contactsapp.database.ItemRoomDatabase.Companion.getDatabase
import com.example.contactsapp.repository.ContactsRepository
import kotlinx.coroutines.launch
import java.io.IOException

enum class ApiStatus { LOADING, ERROR, DONE }

class ContactsViewModel(application: Application) : ViewModel() {

    val userInfoLiveData = MutableLiveData<UserInfo>()

    private val contactsRepository = ContactsRepository(getDatabase(application))

    val contacts = contactsRepository.contacts.asLiveData()

    private val status = MutableLiveData<ApiStatus>()
    private var _eventNetworkError = MutableLiveData(false)
    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError
    private var _isNetworkErrorShown = MutableLiveData(false)
    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    init {
        refreshDataFromRepository()
    }

    private fun refreshDataFromRepository() {
        viewModelScope.launch {
            try {
                contactsRepository.refreshContacts()
                _eventNetworkError.value = false
                _isNetworkErrorShown.value = false

            } catch (netWorkError: IOException) {
                if (contacts.value.isNullOrEmpty())
                    _eventNetworkError.value = true
            }
        }
    }

    fun addOrRemoveFavorite(contactId: String) {
        viewModelScope.launch {
            contactsRepository.addOrRemoveFavorite(contactId)
        }
    }

    fun getUserDetails(contactId: String) {
        viewModelScope.launch {
            status.value = ApiStatus.LOADING
            try {
                userInfoLiveData.value = UserInfoApi.retrofitService.getUserData(contactId)
                status.value = ApiStatus.DONE
            } catch (e: Exception) {
                status.value = ApiStatus.ERROR
            }
        }
    }

    fun onNetWorkErrorShown() {
        _isNetworkErrorShown.value = true
    }

    class ContactsViewModelFactory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ContactsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ContactsViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}

/*
fun updateItem(
        id: String,
        firstName: String,
        lastName: String,
        picture: String,
        title: String,
        isFavorite: Boolean
    ) {
        val updatedItem = contactsRepository.getUpdatedItemEntry(id, firstName, lastName, picture, title, isFavorite)
        updateItem(updatedItem)
    }

fun getContactsList() {
    viewModelScope.launch {
        status.value = ApiStatus.LOADING
        try {
            contactsListLiveData.value = ContactsListApi.retrofitService.getData()
            innerContactsList = contactsListLiveData.value!!
            innerContactsList.data.sortBy { it.firstName }
            status.value = ApiStatus.DONE
        } catch (e: Exception) {
            status.value = ApiStatus.ERROR
        }
    }
}

    fun addOrRemoveFavorite old (contactId: String) {
        val index = contacts.value?.indexOfFirst { it.id == contactId }
        val currentContact = innerContactsList.data.find { it.id == contactId }
        if (currentContact != null) {
            if (currentContact.isFavorite) {
                val updatedContact = innerContactsList.data[index].copy(isFavorite = false)
                innerContactsList.data = ArrayList(innerContactsList.data)
                innerContactsList.data[index] = updatedContact
                innerContactsList.data.sortWith(compareBy({ it.firstName }, { it.isFavorite }))
            } else {
                val updatedContact = innerContactsList.data[index].copy(isFavorite = true)
                innerContactsList.data = ArrayList(innerContactsList.data)
                innerContactsList.data[index] = updatedContact
                innerContactsList.data.sortWith(compareBy({ it.firstName }, { it.isFavorite }))
            }
        }
        innerContactsList.data.sortByDescending { it.isFavorite }

        contactsListLiveData.value = innerContactsList
    }
*/


