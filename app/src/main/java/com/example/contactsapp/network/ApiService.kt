package com.example.contactsapp

import com.example.contactsapp.data.UserInfo
import com.example.contactsapp.network.NetworkContactsContainer
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

private const val BASE_URL = "https://dummyapi.io/data/v1/"
private const val KEY = "61e0434bb659e5c20f64e9e6"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

var logging: HttpLoggingInterceptor = HttpLoggingInterceptor()
    .setLevel(HttpLoggingInterceptor.Level.BODY)

var httpClient: OkHttpClient.Builder = OkHttpClient.Builder()
    .addInterceptor(logging)

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .client(httpClient.build())
    .build()

interface ContactsListApiService {
    @Headers("app-id: $KEY")
    @GET("user")
    suspend fun getData(): NetworkContactsContainer
}

object ContactsListApi {
    val retrofitService: ContactsListApiService by lazy { retrofit.create(ContactsListApiService::class.java) }
}

interface UserInfoApiService {
    @Headers("app-id: $KEY")
    @GET("user/{id}")
    suspend fun getUserData(@Path("id") contactId: String): UserInfo
}

object UserInfoApi {
    val retrofitService: UserInfoApiService by lazy { retrofit.create(UserInfoApiService::class.java) }
}