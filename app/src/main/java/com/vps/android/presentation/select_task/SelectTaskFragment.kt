package com.vps.android.presentation.select_task

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vps.android.R
import com.vps.android.core.ext.viewBinding
import com.vps.android.core.ext.visible
import com.vps.android.core.recycler.BaseDelegationAdapter
import com.vps.android.core.utils.Notify
import com.vps.android.core.utils.setSafeOnClickListener
import com.vps.android.databinding.FragmentSelectTaskBinding
import com.vps.android.domain.mechanism.MechanismItem
import com.vps.android.domain.task.CreateTaskItem
import com.vps.android.domain.task.FullGoodItem
import com.vps.android.domain.task.PlaceItem
import com.vps.android.domain.task.TaskTypeItem
import com.vps.android.presentation.base.BaseFragment
import com.vps.android.presentation.base.IViewModelState
import com.vps.android.presentation.base.StateBinding
import com.vps.android.presentation.select_task.adapter.SelectTaskParameterAdapterDelegates
import com.vps.android.presentation.select_task.feature.SelectTaskFeature
import com.vps.android.presentation.select_task.feature.SelectTaskState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class SelectTaskFragment : BaseFragment<SelectTaskViewModel>(R.layout.fragment_select_task) {

    private val binding by viewBinding(FragmentSelectTaskBinding::bind)
    private val args: SelectTaskFragmentArgs by navArgs()

    override val viewModel: SelectTaskViewModel by viewModel { parametersOf(args.spec) }

    override val stateBinding by lazy { SelectTaskBinding() }
    private val adapter by lazy { createAdapter() }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.onSave(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        viewModel.onRestore(savedInstanceState)
    }

    override fun setupViews() {
        binding.btnSave.isVisible = args.spec.screenFilter == SELECT_SCREEN_TECH

        val dividerItemDecoration = DividerItemDecoration(requireContext(), RecyclerView.VERTICAL)
        val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.divider_list)
        drawable?.let { dividerItemDecoration.setDrawable(it) }

        binding.recyclerView.apply {
            addItemDecoration(dividerItemDecoration)
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = this@SelectTaskFragment.adapter
        }

        binding.btnSave.setSafeOnClickListener {
            val selectedMechanismList: ArrayList<MechanismItem> = arrayListOf()
            SelectTaskParameterAdapterDelegates.selectedTechIds.forEach { id ->
                viewModel.state.value.taskTechList.firstOrNull { it.id == id }?.let { item ->
                    selectedMechanismList.add(item)
                }
            }
            setFragmentResult(SELECT_TASK_PARAMETER, bundleOf(
                SELECT_PARAMETER_SCREEN to args.spec.screenFilter,
                SELECT_ITEM to selectedMechanismList)
            )
            viewModel.navigateBack()
        }

        binding.btnBack.setSafeOnClickListener {
            viewModel.navigateBack()
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

    private fun handleEvent(event: SelectTaskFeature.Event) {
        when (event) {
            is SelectTaskFeature.Event.Logout -> {
                viewModel.toAuthScreen()
            }
            is SelectTaskFeature.Event.Error -> {
                event.error.message?.let { viewModel.notify(Notify.Text(it)) }
            }
        }
    }

    private fun createAdapter() = BaseDelegationAdapter(
        SelectTaskParameterAdapterDelegates.taskDelegate(::onTaskItemClicked),
        SelectTaskParameterAdapterDelegates.loadingDelegate(::onTaskItemClicked),
        SelectTaskParameterAdapterDelegates.goodDelegate(::onTaskItemClicked),
        SelectTaskParameterAdapterDelegates.techDelegate(::onTechItemClicked),
    )

    private fun onTaskItemClicked(taskTypeItem: CreateTaskItem) {
        binding.recyclerView.post {
            adapter.notifyItemRangeChanged(0, adapter.items.size)
        }
        setFragmentResult(SELECT_TASK_PARAMETER, bundleOf(
            SELECT_PARAMETER_SCREEN to args.spec.screenFilter,
            SELECT_ITEM to taskTypeItem)
        )
        viewModel.navigateBack()
    }

    private fun onTechItemClicked(mechanismItem: MechanismItem) {
        binding.recyclerView.post {
            adapter.notifyItemRangeChanged(0, adapter.items.size)
        }
    }

    private fun renderTaskType(isLoading: Boolean, taskTypesList: List<TaskTypeItem>) {
        binding.progressBar.visible(isLoading)
        if (isLoading) {
            binding.recyclerView.visible(false)
            binding.noItems.visible(false)
        }

        if (taskTypesList.isNotEmpty()) {
            binding.noItems.visible(false)
            binding.recyclerView.visible(true)

            SelectTaskParameterAdapterDelegates.selectedTypeId = args.spec.taskTypeItem?.id ?: -1
            adapter.items = taskTypesList
        } else {
            binding.recyclerView.visible(false)
            binding.noItems.visible(true)
        }
    }

    private fun renderTaskPlaces(isLoading: Boolean, taskPlacesList: List<PlaceItem>, selectedId: Int) {
        binding.progressBar.visible(isLoading)
        if (isLoading) {
            binding.recyclerView.visible(false)
            binding.noItems.visible(false)
        }

        if (taskPlacesList.isNotEmpty()) {
            binding.noItems.visible(false)
            binding.recyclerView.visible(true)

            SelectTaskParameterAdapterDelegates.selectedTypeId = selectedId
            adapter.items = taskPlacesList
        } else {
            binding.recyclerView.visible(false)
            binding.noItems.visible(true)
        }
    }

    private fun renderTaskGoods(isLoading: Boolean, taskGoodsList: List<FullGoodItem>) {
        binding.progressBar.visible(isLoading)
        if (isLoading) {
            binding.recyclerView.visible(false)
            binding.noItems.visible(false)
        }

        if (taskGoodsList.isNotEmpty()) {
            binding.noItems.visible(false)
            binding.recyclerView.visible(true)

            SelectTaskParameterAdapterDelegates.selectedTypeId = args.spec.goodItem?.id ?: -1
            adapter.items = taskGoodsList
        } else {
            binding.recyclerView.visible(false)
            binding.noItems.visible(true)
        }
    }

    private fun renderTaskTech(isLoading: Boolean, taskTechList: List<MechanismItem>) {
        binding.progressBar.visible(isLoading)
        if (isLoading) {
            binding.recyclerView.visible(false)
            binding.noItems.visible(false)
        } else if (taskTechList.isNotEmpty()) {
            binding.noItems.visible(false)
            binding.recyclerView.visible(true)

            val selectedItemsId = args.spec.taskTechList?.map { it.id } ?: listOf()
            SelectTaskParameterAdapterDelegates.selectedTechIds.apply {
                clear()
                addAll(selectedItemsId)
            }
            adapter.items = taskTechList
        } else {
            binding.recyclerView.visible(false)
            binding.noItems.visible(true)
        }
    }

    inner class SelectTaskBinding : StateBinding() {

        override fun bind(data: IViewModelState) {
            data as SelectTaskState

            when (args.spec.screenFilter) {
                SELECT_SCREEN_TYPE -> renderTaskType(data.isLoading, data.taskTypesList)
                SELECT_SCREEN_LOADING_PLACE -> renderTaskPlaces(data.isLoading, data.taskPlacesList, args.spec.loadingPlace?.id ?: -1)
                SELECT_SCREEN_UNLOADING_PLACE -> renderTaskPlaces(data.isLoading, data.taskPlacesList, args.spec.unloadingPlace?.id ?: -1)
                SELECT_SCREEN_GOOD -> renderTaskGoods(data.isLoading, data.taskGoodsList)
                SELECT_SCREEN_TECH -> renderTaskTech(data.isLoading, data.taskTechList)
            }
        }
    }

    companion object {
        const val SELECT_TASK_PARAMETER = "SELECT_TASK_PARAMETER"
        const val SELECT_PARAMETER_SCREEN = "SELECT_PARAMETER_SCREEN"
        const val SELECT_ITEM = "SELECT_ITEM"

        const val SELECT_SCREEN_TYPE = "SELECT_SCREEN_TYPE"
        const val SELECT_SCREEN_LOADING_PLACE = "SELECT_SCREEN_LOADING_PLACE"
        const val SELECT_SCREEN_UNLOADING_PLACE = "SELECT_SCREEN_UNLOADING_PLACE"
        const val SELECT_SCREEN_GOOD = "SELECT_SCREEN_GOOD"
        const val SELECT_SCREEN_TECH = "SELECT_SCREEN_TECH"
    }

}
