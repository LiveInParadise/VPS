package com.vps.android.presentation.mechanism_type

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vps.android.R
import com.vps.android.core.ext.viewBinding
import com.vps.android.core.ext.visible
import com.vps.android.core.local.PrefManager
import com.vps.android.core.recycler.BaseDelegationAdapter
import com.vps.android.core.utils.setSafeOnClickListener
import com.vps.android.databinding.FragmentMechanismTypeBinding
import com.vps.android.domain.mechanism.MechanismType
import com.vps.android.presentation.base.BaseFragment
import com.vps.android.presentation.base.IViewModelState
import com.vps.android.presentation.base.StateBinding
import com.vps.android.presentation.mechanism_type.adapter.MechanismTypeAdapterDelegates
import com.vps.android.presentation.mechanism_type.feature.MechanismTypeFeature
import com.vps.android.presentation.mechanism_type.feature.MechanismTypeState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MechanismTypeFragment : BaseFragment<MechanismTypeViewModel>(R.layout.fragment_mechanism_type) {

    private val binding by viewBinding(FragmentMechanismTypeBinding::bind)
    override val viewModel: MechanismTypeViewModel by viewModel()

    override val stateBinding by lazy { MechanismTypeBinding() }
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
            adapter = this@MechanismTypeFragment.adapter
            setHasFixedSize(true)
        }

        binding.btnLogout.setSafeOnClickListener {
            showLogoutDialog()
        }
    }

    private fun onItemClicked(mechanismType: MechanismType) {
        prefManager.userMechanismType = mechanismType
        viewModel.openChooseMechanismScreen(mechanismType.id)
    }

    override fun initObservers() {
        requireActivity().onBackPressedDispatcher.addCallback(this, backPressedCallback)

        lifecycleScope.launchWhenResumed {
            viewModel.state
                .onEach { stateBinding.bind(it) }
                .launchIn(this)
            viewModel.events
                .onEach { handleEvent(it) }
                .launchIn(this)
        }
    }

    private fun handleEvent(event: MechanismTypeFeature.Event) {
        when (event) {
            is MechanismTypeFeature.Event.Logout -> {
                viewModel.toAuthScreen()
            }
        }
    }

    private fun createAdapter() = BaseDelegationAdapter(
        MechanismTypeAdapterDelegates.mechanismTypeDelegate(::onItemClicked)
    )

    private fun renderData(isLoading: Boolean, list: List<MechanismType>) {
        binding.progressBar.visible(isLoading)
        if (isLoading) {
            binding.recyclerView.visible(false)
            binding.noItems.visible(false)
        } else if (list.isNotEmpty()) {
            binding.noItems.visible(false)
            binding.recyclerView.visible(true)
            adapter.items = list
        } else {
            binding.recyclerView.visible(false)
            binding.noItems.visible(true)
        }
    }

    private val backPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {}
        }

    inner class MechanismTypeBinding : StateBinding() {

        override fun bind(data: IViewModelState) {
            data as MechanismTypeState

            renderData(data.isLoading, data.list)
        }
    }

}
