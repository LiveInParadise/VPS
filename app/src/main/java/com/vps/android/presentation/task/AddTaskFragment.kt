package com.vps.android.presentation.task

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.vps.android.R
import com.vps.android.core.delegates.RenderProp
import com.vps.android.core.ext.viewBinding
import com.vps.android.core.ext.visible
import com.vps.android.core.utils.Notify
import com.vps.android.core.utils.setSafeOnClickListener
import com.vps.android.databinding.FragmentAddTaskBinding
import com.vps.android.domain.mechanism.MechanismTypeClass
import com.vps.android.domain.task.TaskInfo
import com.vps.android.domain.task.TaskTypeClass
import com.vps.android.presentation.base.BaseFragment
import com.vps.android.presentation.base.IViewModelState
import com.vps.android.presentation.base.StateBinding
import com.vps.android.presentation.select_task.SelectTaskFragment
import com.vps.android.presentation.task.feature.AddTaskFeature
import com.vps.android.presentation.task.feature.AddTaskState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class AddTaskFragment : BaseFragment<AddTaskViewModel>(R.layout.fragment_add_task) {

    private val binding by viewBinding(FragmentAddTaskBinding::bind)
    private val args: AddTaskFragmentArgs by navArgs()

    override val viewModel: AddTaskViewModel by viewModel { parametersOf(args.spec) }

    override val stateBinding by lazy { AddTaskBinding() }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.onSave(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        viewModel.onRestore(savedInstanceState)
    }

    override fun setupViews() {
        binding.containerTaskType.setSafeOnClickListener {
            viewModel.openSelectTaskTypeScreen()
        }
        binding.containerTaskLoading.setSafeOnClickListener {
            viewModel.openSelectTaskLoadingScreen()
        }
        binding.containerTaskUnloading.setSafeOnClickListener {
            viewModel.openSelectTaskUnLoadingScreen()
        }
        binding.containerTaskGood.setSafeOnClickListener {
            viewModel.openSelectTaskGoodScreen()
        }
        binding.containerTaskTech.setSafeOnClickListener {
            viewModel.openSelectTechGoodScreen()
        }

        binding.btnCreateTask.setSafeOnClickListener {
            viewModel.checkAndProcessTask()
        }

        binding.btnBack.setSafeOnClickListener {
            viewModel.navigateBack()
        }

        binding.btnLogout.setSafeOnClickListener {
            showLogoutDialog()
        }
    }

    override fun initObservers() {
        initListeners()

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
        setFragmentResultListener(SelectTaskFragment.SELECT_TASK_PARAMETER) { _, bundle ->
            viewModel.onSelectTaskBundle(bundle)
        }
    }

    private fun handleEvent(event: AddTaskFeature.Event) {
        when (event) {
            is AddTaskFeature.Event.Logout -> {
                viewModel.toAuthScreen()
            }
            is AddTaskFeature.Event.LogoutWithActiveTaskError -> {
                viewModel.notify(Notify.Text(getString(R.string.main_item_logout_with_active_task_error)))
            }
            is AddTaskFeature.Event.ShowNotFillError -> {
                viewModel.notify(Notify.Text(getString(R.string.create_task_fill_error)))
            }
            is AddTaskFeature.Event.CreateTaskComplete -> {
                sendFragmentResult(event.taskInfo)
                viewModel.navigateBack()
            }
            is AddTaskFeature.Event.Error -> {
                event.error.message?.let { viewModel.notify(Notify.Text(it)) }
            }
        }
    }

    private fun sendFragmentResult(taskInfo: TaskInfo) {
        setFragmentResult(CREATE_TASK, bundleOf(KEY_DATA to taskInfo))
    }

    private fun renderMechanismType(mechanismType: MechanismTypeClass) {
        when (mechanismType) {
            MechanismTypeClass.SIMPLE -> {
                binding.containerTaskTech.visible(true)
            }
            MechanismTypeClass.COMBINED -> {
                binding.containerTaskTech.visible(false)
            }
        }
    }

    private fun renderTaskTypeClass(taskTypeClass: TaskTypeClass) {
        when (taskTypeClass) {
            TaskTypeClass.SIMPLE_EDIT_NOT_ACTIVE, TaskTypeClass.SIMPLE_NEW -> {
                binding.containerTaskType.isClickable = true
                binding.containerTaskLoading.isClickable = true
                binding.containerTaskUnloading.isClickable = true
                binding.containerTaskGood.isClickable = true
                binding.containerTaskTech.isClickable = true

                binding.ivArrowType.visible(true)
                binding.ivArrowLoading.visible(true)
                binding.ivArrowUnloading.visible(true)
                binding.ivArrowGood.visible(true)
                binding.ivArrowTech.visible(true)
            }
            TaskTypeClass.SIMPLE_EDIT_ACTIVE -> {
                binding.containerTaskType.isClickable = false
                binding.containerTaskLoading.isClickable = false
                binding.containerTaskUnloading.isClickable = false
                binding.containerTaskGood.isClickable = false
                binding.containerTaskTech.isClickable = true

                binding.ivArrowType.visible(false)
                binding.ivArrowLoading.visible(false)
                binding.ivArrowUnloading.visible(false)
                binding.ivArrowGood.visible(false)
                binding.ivArrowTech.visible(true)
            }

            TaskTypeClass.COMBINED_EDIT_NOT_ACTIVE, TaskTypeClass.COMBINED_NEW -> {
                binding.containerTaskType.isClickable = true
                binding.containerTaskLoading.isClickable = true
                binding.containerTaskGood.isClickable = true
                binding.containerTaskUnloading.isClickable = true

                binding.ivArrowType.visible(true)
                binding.ivArrowLoading.visible(true)
                binding.ivArrowGood.visible(true)
                binding.ivArrowUnloading.visible(true)
            }
            TaskTypeClass.COMBINED_EDIT_ACTIVE -> {
                binding.containerTaskType.isClickable = false
                binding.containerTaskLoading.isClickable = false
                binding.containerTaskGood.isClickable = false
                binding.containerTaskUnloading.isClickable = true

                binding.ivArrowType.visible(false)
                binding.ivArrowLoading.visible(false)
                binding.ivArrowGood.visible(false)
                binding.ivArrowUnloading.visible(true)
            }
        }
    }

    inner class AddTaskBinding : StateBinding() {

        private var createTaskLoading: Boolean by RenderProp(false) { isLoading ->
            binding.btnCreateTask.visible(!isLoading)
            binding.progressBar.visible(isLoading)
        }

        override fun bind(data: IViewModelState) {
            data as AddTaskState

            renderMechanismType(data.mechanismType)
            renderTaskTypeClass(data.taskTypeClass)
            createTaskLoading = data.createTaskLoading
            binding.tvTaskType.text = data.taskType?.name ?: ""
            binding.tvTaskLoading.text = data.loadingPlace?.name ?: ""
            binding.tvTaskUnloading.text = data.unloadingPlace?.name ?: ""
            binding.tvTaskGood.text = data.goodItem?.name ?: ""
            binding.tvTaskTech.text = data.mechanismItemList?.joinToString(", ") { it.name }
        }
    }

    companion object {
        const val CREATE_TASK = "CREATE_TASK"
        const val KEY_DATA = "KEY_DATA"
    }

}
