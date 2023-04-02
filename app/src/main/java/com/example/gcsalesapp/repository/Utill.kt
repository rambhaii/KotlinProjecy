package com.example.gcsalesapp.repository

import android.Manifest
import android.R
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.location.LocationManager
import android.os.Build
import android.provider.Settings
import pub.devrel.easypermissions.EasyPermissions
import java.text.SimpleDateFormat
import java.util.*


object Utill {
    fun setColorFilter(drawable: Drawable, color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            drawable.colorFilter = BlendModeColorFilter(color, BlendMode.SRC_ATOP)
        } else {
            @Suppress("DEPRECATION")
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        }
    }

    fun isColorDark(color: Int): Boolean {
        val darkness: Double =
            1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255
        return if (darkness < 0.5) {
            false // It's a light color
        } else {
            true // It's a dark color
        }
    }

    /*
    dd	Date in numeric value
    E	Day in String (short form. Ex: Mon)
    EEEE	Day in String (full form. Ex: Monday)
    MM	Month in numeric value
    yyyy	Year in numeric value
    LLL	Month in String (short form. Ex: Mar)
    LLLL	Month in String (full form. Ex: March)
    HH	Hour in numeric value (24hrs timing format)
    KK	Hour in numeric value (12hrs timing format)
    mm	Minute in numeric value
    ss	Seconds in numeric value
    aaa	Displays AM or PM (according to 12hrs timing format)
    z	Displays the time zone of the region
     */
    fun getFormatedDate(dateStr: String, fromFormate: String,toFormate: String): String {
        try {

            val format = SimpleDateFormat(fromFormate)
            val date: Date = format.parse(dateStr)

            val simpleDateFormat = SimpleDateFormat(toFormate)
            val dateTime = simpleDateFormat.format(date).toString()
            return dateTime;
        } catch (e: Exception) {
            print(e.message)
        }
        return dateStr;
    }


    fun hasLocationPermissions(context: Context) =
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            EasyPermissions.hasPermissions(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        } else {
            EasyPermissions.hasPermissions(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }


    fun print(message: String?) {
        println(message)
    }

}