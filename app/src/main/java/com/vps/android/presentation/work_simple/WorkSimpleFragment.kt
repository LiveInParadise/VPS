package com.vps.android.presentation.work_simple

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.vps.android.R
import com.vps.android.core.ext.viewBinding
import com.vps.android.core.ext.visible
import com.vps.android.core.recycler.BaseDelegationAdapter
import com.vps.android.core.recycler.GridSpaceItemDecoration
import com.vps.android.core.utils.CommonUtils
import com.vps.android.core.utils.Notify
import com.vps.android.core.utils.setSafeOnClickListener
import com.vps.android.databinding.FragmentWorkSimpleBinding
import com.vps.android.domain.mechanism.MechanismItem
import com.vps.android.domain.task.TaskInfo
import com.vps.android.presentation.base.BaseFragment
import com.vps.android.presentation.base.IViewModelState
import com.vps.android.presentation.base.StateBinding
import com.vps.android.presentation.task.AddTaskFragment
import com.vps.android.presentation.work_simple.adapter.SimpleWorkAdapterDelegates
import com.vps.android.presentation.work_simple.feature.WorkSimpleFeature
import com.vps.android.presentation.work_simple.feature.WorkSimpleState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.util.*
import kotlin.concurrent.schedule

class WorkSimpleFragment : BaseFragment<WorkSimpleViewModel>(R.layout.fragment_work_simple) {

    private val binding by viewBinding(FragmentWorkSimpleBinding::bind)
    private val args: WorkSimpleFragmentArgs by navArgs()

    override val viewModel: WorkSimpleViewModel by viewModel { parametersOf(args.spec) }

    override val stateBinding by lazy { WorkSimpleBinding() }
    private val adapter by lazy { createAdapter() }

    private var timerTask: TimerTask? = null
    private val startTime = Date().time

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
            addItemDecoration(GridSpaceItemDecoration(resources.getDimension(R.dimen.default_margin_double).toInt()))
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = this@WorkSimpleFragment.adapter
        }

        binding.btnEdit.setSafeOnClickListener {
            viewModel.toEditScreen()
        }

        binding.btnStop.setSafeOnClickListener {
            viewModel.stopTask()
        }
    }

    private fun startTimer() {
        timerTask?.cancel()
        timerTask = Timer().schedule(1000, 1000) {
            val currentTime = Date().time
            val dif = currentTime - startTime

            binding.tvTimer.post {
                binding.tvTimer.text = CommonUtils.convertMsToTime(dif)
            }
        }
        timerTask?.run()
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
        setFragmentResultListener(AddTaskFragment.CREATE_TASK) { _, bundle ->
            viewModel.saveResult(bundle)
        }
    }

    private fun handleEvent(event: WorkSimpleFeature.Event) {
        when (event) {
            is WorkSimpleFeature.Event.StopTaskComplete -> {
                SimpleWorkAdapterDelegates.clearSelection()
                viewModel.notify(Notify.Text(event.message))
                viewModel.toMainScreen()
            }
            is WorkSimpleFeature.Event.SelectMechanismSuccess -> {
                viewModel.notify(Notify.Text(event.message))
                SimpleWorkAdapterDelegates.setSelection(event.mechanismId)
                adapter.notifyDataSetChanged()
            }
            is WorkSimpleFeature.Event.Error -> {
                event.error.message?.let { viewModel.notify(Notify.Text(it)) }
            }
        }
    }

    private fun createAdapter() = BaseDelegationAdapter(
        SimpleWorkAdapterDelegates.simpleWorkDelegate(::onItemClicked)
    )

    private fun onItemClicked(mechanismItem: MechanismItem) {
        viewModel.selectMechanism(mechanismItem)
    }

    private val backPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {}
        }

    private fun renderTask(taskInfo: TaskInfo?) {
        taskInfo ?: return

        binding.tvTaskType.text = taskInfo.taskTypeName
        binding.tvTaskLoading.text = taskInfo.loadingPlaceName
        binding.tvTaskGood.text = taskInfo.goodsName

        adapter.items = taskInfo.selectedMechanismsInfo
    }

    private fun renderError(showError: Boolean, errorMessage: String?) {
        if (showError && !errorMessage.isNullOrEmpty()) {
            binding.tvError.visible(true)
            binding.tvError.text = errorMessage
        } else {
            binding.tvError.visible(false, hide = true)
        }
    }

    override fun onStart() {
        super.onStart()
        startTimer()
    }

    override fun onStop() {
        super.onStop()
        timerTask?.cancel()
    }

    inner class WorkSimpleBinding : StateBinding() {

        override fun bind(data: IViewModelState) {
            data as WorkSimpleState

            renderError(data.showError, data.errorMessage)
            renderTask(data.taskInfo)
        }
    }

}
