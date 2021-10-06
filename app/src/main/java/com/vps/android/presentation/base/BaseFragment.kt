package com.vps.android.presentation.base

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.vps.android.R
import com.vps.android.core.ext.hideKeyboard
import com.vps.android.presentation.root.MainActivity

abstract class BaseFragment<T : BaseViewModel>(
    @LayoutRes private val contentLayoutId: Int
) : Fragment(contentLayoutId) {

    open val stateBinding: StateBinding? = null

    abstract val viewModel: T

    open val navHostFragmentId: Int? = null
    var navController: NavController? = null

    val root: MainActivity
        get() = activity as MainActivity

    lateinit var permissionsLauncher: ActivityResultLauncher<Array<out String>>

    abstract fun setupViews()
    abstract fun initObservers()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        permissionsLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions(), ::handleResultPermissions
        )

        viewModel.observeNotifications(viewLifecycleOwner) { root.renderNotification(it) }
        viewModel.observeNavigation(viewLifecycleOwner) { root.viewModel.navigate(it) }
        viewModel.observePermissions(viewLifecycleOwner) { subscribeOnRequestedPermissions(it) }

        navController = findNavController()
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        setupViews()
        initObservers()
        stateBinding?.rebind()
    }

    override fun onDestroyView() {
        hideKeyboard()
        (view as? ViewGroup)?.let(this::unsubscribeAdapters)
        super.onDestroyView()
    }

    private fun unsubscribeAdapters(viewGroup: ViewGroup) {
        viewGroup.children.forEach { view ->
            when (view) {
                is RecyclerView -> view.adapter = null
                is ViewPager2 -> view.adapter = null
                is ViewGroup -> unsubscribeAdapters(view)
            }
        }
    }

    fun showLogoutDialog() {
        AlertDialog.Builder(requireContext()).apply {
            setMessage(R.string.logout_message)
            setPositiveButton(R.string.yes) { dialog, _ ->
                dialog.dismiss()
                viewModel.logout()
            }
            setNegativeButton(R.string.no) { dialog, _ -> dialog.dismiss() }
        }.show()
    }

    private fun subscribeOnRequestedPermissions(list: List<String>) {
        permissionsLauncher.launch(list.toTypedArray())
    }

    private fun handleResultPermissions(result: Map<String, Boolean>) {
        val permissionToGrantAndRationaleMap = result.mapValues { (permission, isGranted) ->
            if (isGranted) true to true
            else false to ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
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

    open fun onPermissionsResult(result: PermissionsResult) {}

}
