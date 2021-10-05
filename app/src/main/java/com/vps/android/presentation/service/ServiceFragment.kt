package com.vps.android.presentation.service

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.vps.android.R
import com.vps.android.core.ext.viewBinding
import com.vps.android.core.utils.setSafeOnClickListener
import com.vps.android.databinding.FragmentServiceBinding
import com.vps.android.presentation.base.BaseFragment
import com.vps.android.presentation.base.IViewModelState
import com.vps.android.presentation.base.StateBinding
import com.vps.android.presentation.service.feature.ServiceFeature
import com.vps.android.presentation.service.feature.ServiceState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class ServiceFragment : BaseFragment<ServiceViewModel>(R.layout.fragment_service) {

    private val binding by viewBinding(FragmentServiceBinding::bind)
    override val viewModel: ServiceViewModel by viewModel()

    override val stateBinding by lazy { ServiceBinding() }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.onSave(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        viewModel.onRestore(savedInstanceState)
    }

    override fun setupViews() {
        binding.btnLogout.setSafeOnClickListener {
            showLogoutDialog()
        }

        binding.btnStopService.setSafeOnClickListener {
            viewModel.stopMechanismService()
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

    private fun handleEvent(event: ServiceFeature.Event) {
        when (event) {
            is ServiceFeature.Event.Logout -> {
                viewModel.toAuthScreen()
            }
            is ServiceFeature.Event.StopMechanismServiceComplete -> {
                viewModel.openMainScreen()
            }
        }
    }

    inner class ServiceBinding : StateBinding() {

        override fun bind(data: IViewModelState) {
            data as ServiceState

        }
    }

}
