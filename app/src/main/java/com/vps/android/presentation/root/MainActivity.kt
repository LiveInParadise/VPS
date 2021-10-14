package com.vps.android.presentation.root

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
import com.vps.android.R
import com.vps.android.core.ext.viewBinding
import com.vps.android.core.utils.LocationCheckType
import com.vps.android.core.utils.Notify
import com.vps.android.databinding.ActivityMainBinding
import com.vps.android.presentation.base.BaseActivity
import com.vps.android.presentation.base.IViewModelState
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit

class MainActivity : BaseActivity<MainVm>() {

    private val binding by viewBinding(ActivityMainBinding::inflate)
    override val viewModel: MainVm by viewModel()

    var saveFullDistance: Boolean = false
    var saveTaskDistance: Boolean = false

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initLocation()
    }

    private fun initLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest.create().apply {
            interval = TimeUnit.SECONDS.toMillis(2)
            fastestInterval = TimeUnit.SECONDS.toMillis(2)
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        try {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest, locationCallback, Looper.myLooper())
            Log.d("MyLogs", "Location started")
        } catch (unlikely: SecurityException) {
            Log.e("Locations", "Lost location permissions. Couldn't remove updates. $unlikely")
        }
    }

    override fun subscribeOnFullDistance(checkType: LocationCheckType) {
        when (checkType) {
            is LocationCheckType.StartFullDistance -> {
                saveFullDistance = true
                startLocationUpdates()
            }
            is LocationCheckType.StopFullDistance -> {
                saveFullDistance = false
                stopLocationUpdates()
            }
        }
    }

    override fun subscribeOnTaskDistance(checkType: LocationCheckType) {
        when (checkType) {
            is LocationCheckType.StartTaskDistance -> {
                saveTaskDistance = true
            }
            is LocationCheckType.StopTaskDistance -> {
                saveTaskDistance = false
            }
        }
    }

    override fun renderNotification(notify: Notify) {
        when (notify) {
            is Notify.Text -> onSimpleSnackbar(notify)
            is Notify.Error -> onErrorSnackbar(notify)
//            is Notify.Action -> onHeaderAction(notify)
//            is Notify.DialogAction -> onDialogAction(notify)
//            is Notify.ErrorView -> onErrorView(notify)
//            is Notify.HideErrorView -> onHideErrorView(notify)
//            is Notify.SimpleToast -> onSimpleToast(notify)
        }
    }

    private fun onSimpleSnackbar(notify: Notify.Text) {
        Snackbar.make(binding.rootLayout, notify.message, Snackbar.LENGTH_SHORT).show()
    }

    private fun onErrorSnackbar(notify: Notify.Error) {
        Snackbar.make(binding.rootLayout, notify.message, Snackbar.LENGTH_INDEFINITE)
            .setBackgroundTint(getColor(R.color.design_default_color_error))
            .setTextColor(getColor(android.R.color.white))
            .setActionTextColor(getColor(android.R.color.white))
            .setAction(notify.errLabel) {
                notify.errHandler?.invoke()
            }.show()
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    private fun stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        Log.d("MyLogs", "Location stopped")
    }

    override fun subscribeOnState(state: IViewModelState) {}

    private var locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            Log.d("MyLogs", "Location - ${locationResult}")
            if (saveFullDistance) {
                viewModel.sendFullDistanceCoordinate(locationResult.lastLocation)
            }
            if (saveTaskDistance) {
                viewModel.sendTaskDistanceCoordinate(locationResult.lastLocation)
            }
        }
    }
}
