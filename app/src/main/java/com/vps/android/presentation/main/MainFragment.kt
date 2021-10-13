package com.vps.android.presentation.main

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vps.android.R
import com.vps.android.core.ext.viewBinding
import com.vps.android.core.ext.visible
import com.vps.android.core.recycler.BaseDelegationAdapter
import com.vps.android.core.recycler.MainHorizontalSpaceItemDecoration
import com.vps.android.core.utils.Notify
import com.vps.android.core.utils.setSafeOnClickListener
import com.vps.android.databinding.FragmentMainBinding
import com.vps.android.domain.mechanism.MechanismTypeClass
import com.vps.android.domain.task.TaskInfo
import com.vps.android.presentation.base.BaseFragment
import com.vps.android.presentation.base.IViewModelState
import com.vps.android.presentation.base.StateBinding
import com.vps.android.presentation.main.adapter.TaskAdapterDelegates
import com.vps.android.presentation.main.feature.MainFeature
import com.vps.android.presentation.main.feature.MainState
import com.vps.android.presentation.task.AddTaskFragment
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.*

class MainFragment : BaseFragment<MainViewModel>(R.layout.fragment_main) {

    private val binding by viewBinding(FragmentMainBinding::bind)
    override val viewModel: MainViewModel by viewModel()

    override val stateBinding by lazy { MainBinding() }
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
        binding.recyclerView.apply {
            addItemDecoration(MainHorizontalSpaceItemDecoration(resources.getDimension(R.dimen.default_margin_double).toInt()))
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adapter = this@MainFragment.adapter
            setHasFixedSize(true)
        }

        binding.btnService.setSafeOnClickListener {
            showServiceDialog()
        }

        binding.btnAddTask.setSafeOnClickListener {
            viewModel.openCreateTaskScreen()
        }

        binding.btnLogout.setSafeOnClickListener {
            showLogoutDialog()
        }
    }

    override fun initObservers() {
        initListeners()
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

    private fun initListeners() {
        setFragmentResultListener(AddTaskFragment.CREATE_TASK) { _, _ ->
            viewModel.getTasksForce()
        }
    }

    private fun handleEvent(event: MainFeature.Event) {
        when (event) {
            is MainFeature.Event.Logout -> {
                viewModel.toAuthScreen()
            }
            is MainFeature.Event.LogoutWithActiveTaskError -> {
                viewModel.notify(Notify.Text(getString(R.string.main_item_logout_with_active_task_error)))
            }
            is MainFeature.Event.StartMechanismServiceComplete -> {
                viewModel.notify(Notify.Text(event.message))
                viewModel.stopWorkUpdate()
                viewModel.openServiceScreen()
            }
            is MainFeature.Event.StartServiceWithActiveTaskError -> {
                viewModel.notify(Notify.Text(getString(R.string.main_item_logout_with_active_task_error)))
            }
            is MainFeature.Event.StartSimpleTaskComplete -> {
                viewModel.notify(Notify.Text(event.message))
                viewModel.openWorkSimpleScreen(event.taskInfo)
            }
            is MainFeature.Event.StartCombinedTaskComplete -> {
                viewModel.notify(Notify.Text(event.message))
                viewModel.getTasks()
            }
            is MainFeature.Event.StartSecondTaskError -> {
                viewModel.notify(Notify.Text(getString(R.string.main_item_start_send_task_error)))
            }
            is MainFeature.Event.StopTaskWithoutUnloadingPlaceError -> {
                viewModel.notify(Notify.Text(getString(R.string.main_item_stop_task_without_unloading_place)))
            }
            is MainFeature.Event.Error -> {
                event.error.message?.let { viewModel.notify(Notify.Text(it)) }
            }
        }
    }

    private fun showServiceDialog() {
        AlertDialog.Builder(requireContext()).apply {
            setMessage(R.string.dialog_service_message)
            setPositiveButton(R.string.yes) { dialog, _ ->
                dialog.dismiss()
                viewModel.startMechanismService()
            }
            setNegativeButton(R.string.no) { dialog, _ -> dialog.dismiss() }
        }.show()
    }

    private fun createAdapter() = BaseDelegationAdapter(
        TaskAdapterDelegates.taskDelegate(::onEditClicked, ::actionClick).apply {
            TaskAdapterDelegates.mechanismTypeClass = viewModel.prefManager.userMechanismType?.getType() ?: MechanismTypeClass.SIMPLE
        }
    )

    private fun onEditClicked(taskInfo: TaskInfo, isWorkStarted: Boolean) {
        viewModel.openCreateTaskScreen(taskInfo, isWorkStarted)
    }

    private fun actionClick(taskInfo: TaskInfo, isStart: Boolean) {
        if (isStart) {
            viewModel.startWorkTask(taskInfo)
        } else {
            viewModel.stopTask(taskInfo)
        }
    }

    private val backPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {}
        }

    private fun renderTasks(isLoading: Boolean, taskItems: List<TaskInfo>) {
        binding.progressBar.visible(isLoading)
        if (isLoading) {
            binding.recyclerView.visible(false)
            binding.noItems.visible(false)
        } else if (taskItems.isNotEmpty()) {
            binding.noItems.visible(false)
            binding.recyclerView.visible(true)
            adapter.setItems(taskItems) {
                binding.recyclerView.scrollToPosition(0)
            }
        } else {
            binding.recyclerView.visible(false)
            binding.noItems.visible(true)
        }
    }

    inner class MainBinding : StateBinding() {

        override fun bind(data: IViewModelState) {
            data as MainState

            renderTasks(data.isLoading, data.taskItems)
        }
    }

}
