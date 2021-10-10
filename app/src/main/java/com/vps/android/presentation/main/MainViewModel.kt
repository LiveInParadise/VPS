package com.vps.android.presentation.main

import android.os.Bundle
import androidx.lifecycle.viewModelScope
import com.vps.android.MainNavigationDirections
import com.vps.android.core.local.PrefManager
import com.vps.android.domain.mechanism.MechanismTypeClass
import com.vps.android.domain.task.TaskInfo
import com.vps.android.domain.task.TaskTypeClass
import com.vps.android.interactors.auth.AuthInteractor
import com.vps.android.interactors.mechanism.MechanismInteractor
import com.vps.android.interactors.task.TaskInteractor
import com.vps.android.presentation.base.BaseViewModel
import com.vps.android.presentation.base.NavigationCommand
import com.vps.android.presentation.main.feature.MainEffectHandler
import com.vps.android.presentation.main.feature.MainFeature
import com.vps.android.presentation.task.AddTaskSpec
import com.vps.android.presentation.work_simple.WorkSimpleSpec
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

class MainViewModel(
    private val taskInteractor: TaskInteractor,
    private val mechanismInteractor: MechanismInteractor,
    private val authInteractor: AuthInteractor,
    val prefManager: PrefManager,
) : BaseViewModel() {

    private val feature = MainFeature()
    val state = feature.state

    protected val _messages: Channel<String> = Channel()
    protected val _events: Channel<MainFeature.Event> = Channel()
    val events: Flow<MainFeature.Event> = _events.receiveAsFlow()

    init {
        feature.init(viewModelScope, MainEffectHandler(taskInteractor, mechanismInteractor, authInteractor, _events, _messages))
        initMechanism()
        getTasks()
    }

    private fun initMechanism() {
        val type = prefManager.userMechanismType?.getType() ?: MechanismTypeClass.SIMPLE
        feature.act(MainFeature.Action.InitMechanism(type))
    }

    fun getTasks() {
        feature.act(MainFeature.Action.GetTaskList)
    }

    override fun logout() {
        feature.act(MainFeature.Action.Logout)
    }

    fun startMechanismService() {
        feature.act(MainFeature.Action.StartMechanismService)
    }

    fun startWorkTask(taskInfo: TaskInfo) {
        feature.act(MainFeature.Action.StartTask(taskInfo.id))
    }

    fun stopTask(taskInfo: TaskInfo) {
        feature.act(MainFeature.Action.StopTask(taskInfo.id))
    }

    fun toAuthScreen() {
        val dir = MainNavigationDirections.actionLogout()
        navigate(NavigationCommand.Dir(dir))
    }

    fun openServiceScreen() {
        val dir = MainNavigationDirections.actionToService()
        navigate(NavigationCommand.Dir(dir))
    }

    fun openCreateTaskScreen(taskInfo: TaskInfo? = null, isWorkStarted: Boolean = false) {
        val mechanismType = prefManager.userMechanismType?.getType() ?: MechanismTypeClass.SIMPLE
        val spec = when (mechanismType) {
            MechanismTypeClass.SIMPLE -> {
                AddTaskSpec(
                    taskId = taskInfo?.id,
                    mechanismTypeClass = mechanismType,
                    taskTypeClass = if (taskInfo != null) TaskTypeClass.SIMPLE_EDIT_NOT_ACTIVE else TaskTypeClass.SIMPLE_NEW,
                    taskType = taskInfo?.getTaskType(),
                    loadingPlace = taskInfo?.getLoadingPlace(),
                    unloadingPlace = taskInfo?.getUnLoadingPlace(),
                    goodItem = taskInfo?.getGoodItem(),
                    mechanismItemList = taskInfo?.selectedMechanismsInfo,
                )
            }
            MechanismTypeClass.COMBINED -> {
                val typeClass = if (taskInfo != null && isWorkStarted) {
                    TaskTypeClass.COMBINED_EDIT_ACTIVE
                } else if (taskInfo != null && !isWorkStarted) {
                    TaskTypeClass.COMBINED_EDIT_NOT_ACTIVE
                } else {
                    TaskTypeClass.COMBINED_NEW
                }
                AddTaskSpec(
                    taskId = taskInfo?.id,
                    mechanismTypeClass = mechanismType,
                    taskTypeClass = typeClass
                )
            }
        }
        val dir = MainNavigationDirections.actionToAddTask(spec)
        navigate(NavigationCommand.Dir(dir))
    }

    fun openWorkSimpleScreen(taskInfo: TaskInfo) {
        val spec = WorkSimpleSpec(taskInfo = taskInfo)
        val dir = MainNavigationDirections.actionToWorkSimple(spec)
        navigate(NavigationCommand.Dir(dir))
    }

    fun onSave(outState: Bundle) {
        feature.save(outState)
    }

    fun onRestore(savedInstanceState: Bundle?) {
        feature.restore(savedInstanceState)
    }
}
