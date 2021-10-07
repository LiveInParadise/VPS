package com.vps.android.presentation.task

import android.os.Bundle
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
            viewModel.checkAndCreateTask()
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
            is AddTaskFeature.Event.ShowNotFillError -> {
                viewModel.notify(Notify.Text(getString(R.string.create_task_fill_error)))
            }
            is AddTaskFeature.Event.CreateTaskComplete -> {
                viewModel.notify(Notify.Text(event.message))
                viewModel.openMainScreen()
            }
            is AddTaskFeature.Event.Error -> {
                event.error.message?.let { viewModel.notify(Notify.Text(it)) }
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

            createTaskLoading = data.createTaskLoading
            binding.tvTaskType.text = data.taskType?.name ?: ""
            binding.tvTaskLoading.text = data.loadingPlace?.name ?: ""
            binding.tvTaskUnloading.text = data.unloadingPlace?.name ?: ""
            binding.tvTaskGood.text = data.goodItem?.name ?: ""
            binding.tvTaskTech.text = data.mechanismItemList?.joinToString(", ") { it.name }
        }
    }

}
