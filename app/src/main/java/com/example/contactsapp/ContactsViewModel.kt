package com.example.contactsapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contactsapp.data.SubData
import com.example.contactsapp.data.UserInfo
import kotlinx.coroutines.launch

enum class ApiStatus { LOADING, ERROR, DONE }

class ContactsViewModel : ViewModel() {

    private val status = MutableLiveData<ApiStatus>()
    val contactsListLiveData = MutableLiveData<SubData>()
    val UserInfoLiveData = MutableLiveData<UserInfo>()

    fun getContactsList() {
        viewModelScope.launch {
            status.value = ApiStatus.LOADING
            try {
                contactsListLiveData.value = ContactsListApi.retrofitService.getData()
                status.value = ApiStatus.DONE
            } catch (e: Exception) {
                status.value = ApiStatus.ERROR
            }
        }
    }

    fun getUserInfo(contactId: String) {
        viewModelScope.launch {
            status.value = ApiStatus.LOADING
            try {
                UserInfoLiveData.value = UserInfoApi.retrofitService.getUserData(contactId)
                status.value = ApiStatus.DONE
            } catch (e: Exception) {
                status.value = ApiStatus.ERROR
            }
        }
    }
}