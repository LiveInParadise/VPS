package com.vps.android.presentation.mechanism

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vps.android.R
import com.vps.android.core.ext.viewBinding
import com.vps.android.core.ext.visible
import com.vps.android.core.local.PrefManager
import com.vps.android.core.recycler.BaseDelegationAdapter
import com.vps.android.core.utils.setSafeOnClickListener
import com.vps.android.databinding.FragmentMechanismBinding
import com.vps.android.domain.mechanism.MechanismItem
import com.vps.android.presentation.base.BaseFragment
import com.vps.android.presentation.base.IViewModelState
import com.vps.android.presentation.base.StateBinding
import com.vps.android.presentation.mechanism.adapter.MechanismAdapterDelegates
import com.vps.android.presentation.mechanism.feature.MechanismFeature
import com.vps.android.presentation.mechanism.feature.MechanismState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MechanismFragment : BaseFragment<MechanismViewModel>(R.layout.fragment_mechanism) {

    private val binding by viewBinding(FragmentMechanismBinding::bind)
    private val args: MechanismFragmentArgs by navArgs()

    override val viewModel: MechanismViewModel by viewModel { parametersOf(args.spec) }

    override val stateBinding by lazy { MechanismBinding() }
    private val adapter by lazy { createAdapter() }

    private val prefManager: PrefManager by inject()

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.onSave(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        viewModel.onRestore(savedInstanceState)
    }

    override fun setupViews() {
        val dividerItemDecoration = DividerItemDecoration(requireContext(), RecyclerView.VERTICAL)
        val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.divider_list)
        drawable?.let { dividerItemDecoration.setDrawable(it) }

        binding.recyclerView.apply {
            addItemDecoration(dividerItemDecoration)
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = this@MechanismFragment.adapter
            setHasFixedSize(true)
        }

        binding.btnBack.setSafeOnClickListener {
            viewModel.navigateBack()
        }

        binding.btnLogout.setSafeOnClickListener {
            showLogoutDialog()
        }
    }

    private fun onItemClicked(mechanismItem: MechanismItem) {
        viewModel.selectMechanism(mechanismItem)
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

    private fun handleEvent(event: MechanismFeature.Event) {
        when (event) {
            is MechanismFeature.Event.MechanismInService -> {
                viewModel.openServiceScreen()
            }
            is MechanismFeature.Event.MechanismNotInService -> {
                viewModel.openMainScreen()
            }
            is MechanismFeature.Event.Logout -> {
                viewModel.toAuthScreen()
            }
        }
    }

    private fun createAdapter() = BaseDelegationAdapter(
        MechanismAdapterDelegates.mechanismDelegate(::onItemClicked)
    )

    private fun renderData(isLoading: Boolean, list: List<MechanismItem>) {
        binding.progressBar.visible(isLoading)
        if (isLoading) {
            binding.recyclerView.visible(false)
            binding.noItems.visible(false)
        }

        if (list.isNotEmpty()) {
            binding.noItems.visible(false)
            binding.recyclerView.visible(true)
            adapter.items = list
        } else {
            binding.recyclerView.visible(false)
            binding.noItems.visible(true)
        }
    }

    inner class MechanismBinding : StateBinding() {

        override fun bind(data: IViewModelState) {
            data as MechanismState

            renderData(data.isLoading, data.list)
        }
    }

}
