package com.example.gcsalesapp.di

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.gcsalesapp.R
import com.example.gcsalesapp.data.Constants
import com.example.gcsalesapp.ui.view.HomeActivity
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped

@Module
@InstallIn(ServiceComponent::class)
object ServiceModule {

    @ServiceScoped
    @Provides
    fun provideFusedLocationProviderClient(@ApplicationContext app: Context) =
        FusedLocationProviderClient(app)


    @ServiceScoped
    @Provides
    fun providePendingIntent(@ApplicationContext app: Context) =
        PendingIntent.getActivity(
            app,
            0,
            Intent(app, HomeActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT
        )

    @ServiceScoped
    @Provides
    fun provideNotificationBuilder(
        @ApplicationContext app:Context,
        pendingIntent: PendingIntent
    )= NotificationCompat.Builder(app, Constants.NOTIFICATION_CHANNEL_ID)
        .setAutoCancel(false)
        .setOngoing(true)
        .setSmallIcon(R.drawable.ic_grey_logo)
//        .setContentTitle("")
//        .setContentText("00:00:00")
        .setContentIntent(pendingIntent)

}