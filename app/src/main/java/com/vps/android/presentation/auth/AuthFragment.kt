package com.vps.android.presentation.auth

import android.os.Bundle
import com.vps.android.R
import com.vps.android.core.ext.viewBinding
import com.vps.android.databinding.FragmentAuthBinding
import com.vps.android.presentation.auth.feature.AuthFeature
import com.vps.android.presentation.auth.feature.AuthState
import com.vps.android.presentation.base.BaseFragment
import com.vps.android.presentation.base.IViewModelState
import com.vps.android.presentation.base.StateBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class AuthFragment : BaseFragment<AuthViewModel>(R.layout.fragment_auth) {

    private val binding by viewBinding(FragmentAuthBinding::bind)
    override val viewModel: AuthViewModel by viewModel()

    override val stateBinding by lazy { AuthBinding() }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.onSave(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        viewModel.onRestore(savedInstanceState)
    }

    override fun setupViews() {

    }

    override fun initObservers() {

    }

    private fun handleEvent(event: AuthFeature.Event) {
        when (event) {

        }
    }

    inner class AuthBinding : StateBinding() {

        override fun bind(data: IViewModelState) {
            data as AuthState

        }
    }

}
