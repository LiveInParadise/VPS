package com.vps.android.presentation.base

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.vps.android.R
import com.vps.android.core.utils.LocationCheckType
import com.vps.android.core.utils.Notify

abstract class BaseActivity<T : BaseViewModel> : AppCompatActivity() {

    abstract val viewModel: T
    lateinit var navController: NavController
    lateinit var permissionsLauncher: ActivityResultLauncher<Array<out String>>
    lateinit var settingsLauncher: ActivityResultLauncher<Intent>

    abstract fun subscribeOnState(state: IViewModelState)

    abstract fun renderNotification(notify: Notify)

    abstract fun subscribeOnFullDistance(checkType: LocationCheckType)
    abstract fun subscribeOnTaskDistance(checkType: LocationCheckType)

    override fun setContentView(view: View?) {
        super.setContentView(view)

        permissionsLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions(), ::handleResultPermissions
        )

        settingsLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(), ::handleResultSettings
        )

        viewModel.observeNotifications(this) { renderNotification(it) }
        viewModel.observeNavigation(this) { subscribeOnNavigation(it) }
        viewModel.observePermissions(this) { subscribeOnRequestedPermissions(it) }
        viewModel.observeLocationListener(this, { subscribeOnFullDistance(it) }, { subscribeOnTaskDistance(it) })

        navController = findNavController(R.id.nav_host_fragment_activity_main)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    open fun subscribeOnNavigation(command: NavigationCommand) {
        when (command) {
            is NavigationCommand.Back -> navController.popBackStack()
            is NavigationCommand.To -> {
                navController.navigate(
                    command.destination,
                    command.args,
                    command.options,
                    command.extras
                )
            }
            is NavigationCommand.Dir -> {
                navController.navigate(
                    command.directions,
                    command.options
                )
            }
            is NavigationCommand.DirForward -> {
                navController.navigate(
                    command.directions
                )
                subscribeOnNavigation(
                    NavigationCommand.Dir(
                        command.forwardDirections,
                        command.options
                    )
                )
            }
        }
    }

    private fun subscribeOnRequestedPermissions(list: List<String>) {
        permissionsLauncher.launch(list.toTypedArray())
    }

    private fun handleResultPermissions(result: Map<String, Boolean>) {
        val permissionToGrantAndRationaleMap = result.mapValues { (permission, isGranted) ->
            if (isGranted) true to true
            else false to ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                permission
            )
        }

        val grantRationaleValues = permissionToGrantAndRationaleMap.values
        val isAllGranted = !grantRationaleValues.map { it.first }.contains(false)
        val isAnyPermissionWithRestrictedRationale =
            grantRationaleValues.map { it.second }.contains(false)

        val permissionsResult = PermissionsResult(
            permissionToGrantAndRationaleMap,
            isAllGranted,
            !isAnyPermissionWithRestrictedRationale
        )

        onPermissionsResult(permissionsResult)
    }

    open fun onPermissionsResult(result: PermissionsResult) {

    }

    protected fun openSettings(message: String, actionLabel: String) {
        val errHandler = {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.parse("package:$packageName")
            }
            settingsLauncher.launch(intent)
        }
        viewModel.notify(Notify.Error(message, actionLabel, errHandler))
    }

    private fun handleResultSettings(result: ActivityResult) {

    }
}
