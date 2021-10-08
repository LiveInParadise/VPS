package com.vps.android.presentation.work_simple

import android.os.Bundle
import androidx.lifecycle.viewModelScope
import com.vps.android.MainNavigationDirections
import com.vps.android.domain.mechanism.MechanismItem
import com.vps.android.domain.mechanism.MechanismTypeClass
import com.vps.android.domain.task.EditTaskSourcePage
import com.vps.android.interactors.mechanism.MechanismInteractor
import com.vps.android.interactors.task.TaskInteractor
import com.vps.android.presentation.base.BaseViewModel
import com.vps.android.presentation.base.NavigationCommand
import com.vps.android.presentation.task.AddTaskSpec
import com.vps.android.presentation.work_simple.feature.WorkSimpleEffectHandler
import com.vps.android.presentation.work_simple.feature.WorkSimpleFeature
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

class WorkSimpleViewModel(
    private val spec: WorkSimpleSpec,
    private val taskInteractor: TaskInteractor,
    private val mechanismInteractor: MechanismInteractor,
) : BaseViewModel() {

    private val feature = WorkSimpleFeature()
    val state = feature.state

    protected val _messages: Channel<String> = Channel()
    protected val _events: Channel<WorkSimpleFeature.Event> = Channel()
    val events: Flow<WorkSimpleFeature.Event> = _events.receiveAsFlow()

    init {
        feature.init(viewModelScope, WorkSimpleEffectHandler(taskInteractor, mechanismInteractor, _events, _messages))
        initType()
    }

    private fun initType() {
        feature.act(WorkSimpleFeature.Action.InitTask(spec.taskInfo))
    }

    fun stopTask() {
        feature.act(WorkSimpleFeature.Action.StopTask)
    }

    fun selectMechanism(mechanismItem: MechanismItem) {
        feature.act(WorkSimpleFeature.Action.SelectMechanism(mechanismItem.id))
    }

    fun toMainScreen() {
        val dir = MainNavigationDirections.actionToMain()
        navigate(NavigationCommand.Dir(dir))
    }

    fun toEditScreen() {
        val taskItem = state.value.taskInfo ?: return
        val spec = AddTaskSpec(
            mechanismTypeClass = MechanismTypeClass.SIMPLE,
            taskType = taskItem.getTaskType(),
            loadingPlace = taskItem.getLoadingPlace(),
            unloadingPlace = taskItem.getUnLoadingPlace(),
            goodItem = taskItem.getGoodItem(),
            mechanismItemList = taskItem.selectedMechanismsInfo,
            sourcePage = EditTaskSourcePage.WORK_SIMPLE
        )
        val dir = MainNavigationDirections.actionToAddTask(spec)
        navigate(NavigationCommand.Dir(dir))
    }

    fun onSave(outState: Bundle) {
        feature.save(outState)
    }

    fun onRestore(savedInstanceState: Bundle?) {
        feature.restore(savedInstanceState)
    }

}
