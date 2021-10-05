package com.vps.android.presentation.main

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.vps.android.R
import com.vps.android.core.ext.viewBinding
import com.vps.android.core.utils.setSafeOnClickListener
import com.vps.android.databinding.FragmentMainBinding
import com.vps.android.presentation.base.BaseFragment
import com.vps.android.presentation.base.IViewModelState
import com.vps.android.presentation.base.StateBinding
import com.vps.android.presentation.main.feature.MainFeature
import com.vps.android.presentation.main.feature.MainState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : BaseFragment<MainViewModel>(R.layout.fragment_main) {

    private val binding by viewBinding(FragmentMainBinding::bind)
    override val viewModel: MainViewModel by viewModel()

    override val stateBinding by lazy { MainBinding() }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.onSave(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        viewModel.onRestore(savedInstanceState)
    }

    override fun setupViews() {
        binding.btnService.setSafeOnClickListener {
            viewModel.startMechanismService()
        }

        binding.btnLogout.setSafeOnClickListener {
            showLogoutDialog()
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

    private fun handleEvent(event: MainFeature.Event) {
        when (event) {
            is MainFeature.Event.Logout -> {
                viewModel.toAuthScreen()
            }
            is MainFeature.Event.StartMechanismServiceComplete -> {
                viewModel.openServiceScreen()
            }
        }
    }

    inner class MainBinding : StateBinding() {

        override fun bind(data: IViewModelState) {
            data as MainState

        }
    }

}
