package com.example.gcsalesapp.ui.view

import android.Manifest
import android.content.Intent
import android.content.IntentSender
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.gcsalesapp.data.Constants
import com.example.gcsalesapp.repository.PrefrenceRepository
import com.example.gcsalesapp.repository.Utill
import com.example.gcsalesapp.services.TrackingService
import com.example.gcsalesapp.ui.view.dialogs.CustomMessageDialog
import com.example.gcsalesapp.ui.viewmodal.ApiState
import com.example.gcsalesapp.ui.viewmodal.HomeViewModel
import com.example.gcsalesapp.ui.viewmodal.TrackingViewModel
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.*
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.lang.Exception
import javax.inject.Inject


open class BaseActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    private var googleApiClient: GoogleApiClient? = null
    private val REQUESTLOCATION = 199
    private val messageDialog = CustomMessageDialog()

    private val viewModel: TrackingViewModel by viewModels()
    private val homeViewModel: HomeViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        TrackingService.pathPoints.observe(this, {
           try {
               Utill.print("interval ==${TrackingViewModel.interval}")
               viewModel.getAddress(it)
               viewModel.sendLocationToServer(it)
           }catch (e:Exception){
//               messageDialog.show(this,CustomMessageDialog.MessageType.ERROR,"${e.message}")
           }
        })

        viewModel.validateLocationResp.observe(this, { state ->
            when (state) {
                is ApiState.Failure -> {
                    if(!messageDialog.isShowing) {
                        messageDialog.show(
                            this,
                            CustomMessageDialog.MessageType.ERROR,
                            state.errorText
                        ).setOnDismissListener {
                            if(state.errorCode==2)
                            {
                                homeViewModel.logoutSalesStaff()
                                Intent(this, LoginActivity::class.java).also {
                                    startActivity(it)
                                    overridePendingTransition(
                                        android.R.anim.slide_in_left,
                                        android.R.anim.slide_out_right
                                    )
                                    finish()
                                }
                            }
                            messageDialog.isShowing = false;
                        }
                    }
                }
            }

        })


    }

    override fun onResume() {
        super.onResume()
        requestPermissions()
    }

    private fun enableLoc() {
        googleApiClient = GoogleApiClient.Builder(this)
            .addApi(LocationServices.API)
            .addConnectionCallbacks(object : GoogleApiClient.ConnectionCallbacks {
                override fun onConnected(bundle: Bundle?) {}
                override fun onConnectionSuspended(i: Int) {
                    googleApiClient?.connect()
                }
            })
            .addOnConnectionFailedListener {
            }.build()
        googleApiClient?.connect()
        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 30 * 1000.toLong()
        locationRequest.fastestInterval = 5 * 1000.toLong()
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        builder.setAlwaysShow(true)
        val result: PendingResult<LocationSettingsResult> =
            LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build())
        result.setResultCallback { result ->
            val status: Status = result.status
            when (status.statusCode) {
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                    status.startResolutionForResult(
                        this,
                        REQUESTLOCATION
                    )
                } catch (e: IntentSender.SendIntentException) {
                }
            }
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {}

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            requestPermissions()
        }
    }

    private fun requestPermissions() {
        if (Utill.hasLocationPermissions(this)) {
            enableLoc()
            Intent(this, TrackingService::class.java).also {
                startService(it)
            }
            return
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            EasyPermissions.requestPermissions(
                this,
                "You need to accept location permissions to use this app.",
                Constants.REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        } else {
            EasyPermissions.requestPermissions(
                this,
                "You need to accept location permissions to use this app.",
                Constants.REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }


}