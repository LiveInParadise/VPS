package com.vps.android.presentation.main

import android.os.Bundle
import androidx.lifecycle.viewModelScope
import com.vps.android.MainNavigationDirections
import com.vps.android.core.local.PrefManager
import com.vps.android.domain.mechanism.MechanismTypeClass
import com.vps.android.domain.task.EditTaskSourcePage
import com.vps.android.domain.task.TaskInfo
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
    private val prefManager: PrefManager,
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

    fun toAuthScreen() {
        val dir = MainNavigationDirections.actionLogout()
        navigate(NavigationCommand.Dir(dir))
    }

    fun openServiceScreen() {
        val dir = MainNavigationDirections.actionToService()
        navigate(NavigationCommand.Dir(dir))
    }

    fun openCreateTaskScreen() {
        val spec = AddTaskSpec(
            mechanismTypeClass = prefManager.userMechanismType?.getType() ?: MechanismTypeClass.SIMPLE,
            sourcePage = EditTaskSourcePage.MAIN
        )
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
