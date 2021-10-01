package com.vps.android.presentation.root

import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.snackbar.Snackbar
import com.vps.android.R
import com.vps.android.core.ext.viewBinding
import com.vps.android.core.utils.Notify
import com.vps.android.databinding.ActivityMainBinding
import com.vps.android.presentation.base.BaseActivity
import com.vps.android.presentation.base.IViewModelState
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity<MainVm>() {

    private val binding by viewBinding(ActivityMainBinding::inflate)
    override val viewModel: MainVm by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    override fun renderNotification(notify: Notify) {
        when (notify) {
            is Notify.Text -> onSimpleSnackbar(notify)
//            is Notify.Action -> onHeaderAction(notify)
//            is Notify.DialogAction -> onDialogAction(notify)
//            is Notify.ErrorView -> onErrorView(notify)
//            is Notify.ErrorSnackbar -> onErrorSnackbar(notify)
//            is Notify.HideErrorView -> onHideErrorView(notify)
//            is Notify.SimpleToast -> onSimpleToast(notify)
        }
    }

    private fun onSimpleSnackbar(notify: Notify.Text) {
        Snackbar.make(binding.rootLayout, notify.message, Snackbar.LENGTH_SHORT).show()
    }

    override fun subscribeOnState(state: IViewModelState) {}
}
