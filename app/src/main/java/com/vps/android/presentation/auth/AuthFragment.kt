package com.vps.android.presentation.auth

import android.Manifest
import android.app.Activity
import android.content.Context
import android.location.LocationManager
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isInvisible
import androidx.lifecycle.lifecycleScope
import com.vps.android.R
import com.vps.android.core.delegates.RenderProp
import com.vps.android.core.ext.viewBinding
import com.vps.android.core.ext.visible
import com.vps.android.core.local.PrefManager
import com.vps.android.core.utils.Notify
import com.vps.android.databinding.FragmentAuthBinding
import com.vps.android.presentation.auth.feature.AuthFeature
import com.vps.android.presentation.auth.feature.AuthState
import com.vps.android.presentation.base.BaseFragment
import com.vps.android.presentation.base.IViewModelState
import com.vps.android.presentation.base.PermissionsResult
import com.vps.android.presentation.base.StateBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class AuthFragment : BaseFragment<AuthViewModel>(R.layout.fragment_auth) {

    private val binding by viewBinding(FragmentAuthBinding::bind)
    override val viewModel: AuthViewModel by viewModel()

    override val stateBinding by lazy { AuthBinding() }

    private var locationManager: LocationManager? = null

    private val pref: PrefManager by inject()
    private val pinLength by lazy { resources.getInteger(R.integer.pin_length) }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.onSave(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        viewModel.onRestore(savedInstanceState)
    }

    override fun setupViews() {
        locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        with(binding) {
            otpView.isClickable = false
            otpView.isFocusable = false
            otpView.setOtpCompletionListener {
                viewModel.requestPermissions(listOf(Manifest.permission.ACCESS_FINE_LOCATION))
            }

            keypad.onKeypadClicked = {
                val currentLength = otpView.text?.length ?: 0
                if (currentLength < pinLength) {
                    otpView.append(it)
                }
            }
            keypad.onClearClicked = {
                otpView.setText(otpView.text?.dropLast(1) ?: "")
            }

            androidId.visible(pref.showDeviceId)
            androidId.text = pref.deviceId
        }
    }

    override fun initObservers() {
        lifecycleScope.launchWhenResumed {
            viewModel.state
                .onEach { stateBinding.bind(it) }
                .launchIn(this)
            viewModel.events
                .onEach { handleEvent(it) }
                .launchIn(this)
        }
    }

    private fun handleEvent(event: AuthFeature.Event) {
        when (event) {
            is AuthFeature.Event.AuthSuccess -> {
                pref.showDeviceId = false
                viewModel.notify(Notify.Text(requireContext().getString(R.string.auth_success)))
                viewModel.navigateToSettings()
            }
        }
    }

    private fun renderError(showError: Boolean, errorMessage: String?) {
        binding.tvError.apply {
            isInvisible = !showError
            text = errorMessage ?: getString(R.string.auth_code_error)
        }
        if (showError) binding.otpView.text?.clear()
    }

    override fun onPermissionsResult(result: PermissionsResult) {
        val permissionList = result.permissions.keys.toList()
        when {
            result.allGranted -> checkGPSAndGo()
            !result.canRequestAgain -> openSettings(
                getString(R.string.permissions_settings_message),
                getString(R.string.permissions_settings_action)
            )
            else -> {
                val notif = Notify.Error(
                    getString(R.string.permissions_settings_message),
                    getString(R.string.permissions_retry)
                ) {
                    viewModel.requestPermissions(permissionList)
                }
                viewModel.notify(notif)
            }
        }
    }

    private fun checkGPSAndGo() {
        if (locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER) == false) {
            buildAlertMessageNoGps(requireActivity(), 0)
            return
        }

        binding.otpView.text?.let { viewModel.login(it.toString()) }
    }

    override fun handleResultSettings(result: ActivityResult) {
        viewModel.requestPermissions(listOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
    }

    override fun handleGPSResultSettings(result: ActivityResult) {
        checkGPSAndGo()
    }

    private fun buildAlertMessageNoGps(activity: Activity, requestCode: Int) {
        AlertDialog.Builder(activity)
            .setMessage(R.string.map_gps_disabled)
            .setCancelable(false)
            .setPositiveButton(R.string.yes) { _, _ ->
                openGpsSettings()
            }
            .show()
    }

    inner class AuthBinding : StateBinding() {

        private var isLoading: Boolean by RenderProp(false) { isLoading ->
            binding.progressBar.visible(isLoading)
        }

        private var isKeypadBlocked: Boolean by RenderProp(false) { isKeypadBlocked ->
            binding.keypad.isKeypadEnabled = !isKeypadBlocked
        }

        override fun bind(data: IViewModelState) {
            data as AuthState

            isLoading = data.isLoading
            isKeypadBlocked = data.isKeypadBlocked
            renderError(data.showError, data.errorMessage)
        }
    }

}
