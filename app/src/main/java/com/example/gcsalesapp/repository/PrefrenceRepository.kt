package com.example.gcsalesapp.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import com.example.gcsalesapp.data.Constants
import com.example.gcsalesapp.data.request.MarketingRep
import com.example.gcsalesapp.data.response.SalesStaff
import com.google.gson.Gson
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Named

@ActivityScoped
class PrefrenceRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    @Inject
    @Named("MachineID")
    lateinit var MachineID: String

    suspend fun setLocationInterval(interval:Int) {
        val dataStoreKey = preferencesKey<Int>(Constants.LOCATION_INTERVAL)
        dataStore.edit { settings ->
            settings[dataStoreKey] = interval
        }
    }

    suspend fun getLocationInterval(): Int? {
        val dataStoreKey = preferencesKey<Int>(Constants.LOCATION_INTERVAL)
        val preferences = dataStore.data.first()

        return preferences[dataStoreKey]
    }

    suspend fun setLogin() {
        val dataStoreKey = preferencesKey<Boolean>(Constants.LOGIN_FLAG)
        dataStore.edit { settings ->
            settings[dataStoreKey] = true
        }
    }

    suspend fun setLogout() {
        val dataStoreKey = preferencesKey<Boolean>(Constants.LOGIN_FLAG)
        dataStore.edit { settings ->
            settings[dataStoreKey] = false
        }
    }

    suspend fun isLogin(): Boolean? {
        val dataStoreKey = preferencesKey<Boolean>(Constants.LOGIN_FLAG)
        val preferences = dataStore.data.first()

        return preferences[dataStoreKey]
    }


    suspend fun setAuthKeyData(value: String) {
        val dataStoreKey = preferencesKey<String>(Constants.AUTH_DATA)
        dataStore.edit { settings ->
            settings[dataStoreKey] = value
        }
    }

    suspend fun getAuthKeyData(): String? {
        val dataStoreKey = preferencesKey<String>(Constants.AUTH_DATA)
        val preferences = dataStore.data.first()

        return preferences[dataStoreKey]

    }/*  suspend fun setMarketingRepID(value: String) {
        val dataStoreKey = preferencesKey<String>(Constants.Marketing_RepID)
        dataStore.edit { settings ->
            settings[dataStoreKey] = value
        }
    }

    suspend fun getMarketingRepID(): String? {
        val dataStoreKey = preferencesKey<String>(Constants.Marketing_RepID)
        val preferences = dataStore.data.first()

        return preferences[dataStoreKey]

    }*/


    suspend fun setSalesAgentData(value: SalesStaff) {
        val dataStoreKey = preferencesKey<String>(Constants.SALES_DATA)
        dataStore.edit { settings ->
            settings[dataStoreKey] = Gson().toJson(value)
        }
    }

    suspend fun getSalesAgentData(): SalesStaff {
        val dataStoreKey = preferencesKey<String>(Constants.SALES_DATA)
        val preferences = dataStore.data.first()

//        return  preferences[dataStoreKey]

        val gson = Gson()
        return gson.fromJson(
            preferences[dataStoreKey].toString(),
            SalesStaff::class.java
        )
    }

    suspend fun getMarketingRepData(): MarketingRep {
        val salesStaff = getSalesAgentData()
        return MarketingRep(
            getAuthKeyData()!!,
            salesStaff.MarketingRepID.toString(),
            MachineID,
            salesStaff.PhoneNo
        )
    }

}