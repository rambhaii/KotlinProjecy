package com.example.gcsalesapp.di

import android.content.Context
import android.location.Geocoder
import android.provider.Settings
import androidx.datastore.core.DataStore
import androidx.datastore.createDataStore
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.core.Preferences
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.example.gcsalesapp.R
import com.example.gcsalesapp.api.GcSalesApis
import com.example.gcsalesapp.data.Constants.Companion.BASE_URL
import com.example.gcsalesapp.data.request.MarketingRep
import com.example.gcsalesapp.repository.PrefrenceRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.math.BigInteger
import java.security.MessageDigest
import java.util.*
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideGlideInstance(@ApplicationContext context:Context)=Glide.with(context).setDefaultRequestOptions(
        RequestOptions()
            .placeholder(R.drawable.ic_grey_logo)
            .error(R.drawable.ic_grey_logo)
//            .signature(ObjectKey(yourVersionMetadata))
            .diskCacheStrategy(DiskCacheStrategy.NONE)
    )

    @Singleton
    @Provides
    fun provideRetrofitInstance(): GcSalesApis {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build())
            .baseUrl(BASE_URL)
            .build()
            .create(GcSalesApis::class.java)
    }

    @Singleton
    @Provides
    fun providePreferenceStorage(@ApplicationContext context:Context):DataStore<Preferences>{
        return  context.createDataStore(name = "settings")
    }


    @Singleton
    @Provides
    fun provideGeocoder(@ApplicationContext context:Context): Geocoder {
        return  Geocoder(context, Locale.getDefault())
    }


    @Singleton
    @Provides
    @Named("MachineID")
    fun provideMachineID(@ApplicationContext context:Context):String{
        var device_uuid =
            Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID)
        if (device_uuid == null) {
            device_uuid = "12356789" // for emulator testing
        } else {
            try {
                var _data = device_uuid.toByteArray()
                val _digest = MessageDigest.getInstance("MD5")
                _digest.update(_data)
                _data = _digest.digest()
                val _bi = BigInteger(_data).abs()
                device_uuid = _bi.toString(36)
            } catch (e: Exception) {
                if (e != null) {
                    e.printStackTrace()
                }
            }
        }
        return  device_uuid
    }


}