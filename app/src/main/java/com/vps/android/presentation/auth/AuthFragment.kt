package com.vps.android.presentation.auth

import android.os.Bundle
import androidx.core.view.isInvisible
import androidx.lifecycle.lifecycleScope
import com.vps.android.R
import com.vps.android.core.delegates.RenderProp
import com.vps.android.core.ext.viewBinding
import com.vps.android.core.ext.visible
import com.vps.android.core.utils.Notify
import com.vps.android.databinding.FragmentAuthBinding
import com.vps.android.presentation.auth.feature.AuthFeature
import com.vps.android.presentation.auth.feature.AuthState
import com.vps.android.presentation.base.BaseFragment
import com.vps.android.presentation.base.IViewModelState
import com.vps.android.presentation.base.StateBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
        with(binding) {
            otpView.isClickable = false
            otpView.isFocusable = false
            otpView.setOtpCompletionListener {
                viewModel.login(it)
            }

            keypad.onKeypadClicked = {
                otpView.setText(otpView.text.toString() + it)
            }
            keypad.onClearClicked = {
                otpView.setText(otpView.text?.dropLast(1) ?: "")
            }
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
    }

    inner class AuthBinding : StateBinding() {

        private var isLoading: Boolean by RenderProp(false) { isLoading ->
            binding.progressBar.visible(isLoading)
        }

        private var isKeypadBlocked: Boolean by RenderProp(false) { isKeypadBlocked ->
            binding.keypad.isEnabled = !isKeypadBlocked
        }

        override fun bind(data: IViewModelState) {
            data as AuthState

            isLoading = data.isLoading
            isKeypadBlocked = data.isKeypadBlocked
            renderError(data.showError, data.errorMessage)
        }
    }

}
