package com.vps.android.presentation.main.feature

import com.vps.android.domain.mechanism.MechanismTypeClass
import com.vps.android.domain.task.TaskInfo
import com.vps.android.interactors.task.request.StartTaskRequest
import com.vps.android.interactors.task.request.StopTaskRequest
import com.vps.android.presentation.base.BaseFeature

class MainFeature : BaseFeature<MainState, MainFeature.Action, MainFeature.Effect>() {

    override fun initialState(): MainState = MainState()
    override fun initialEffs(): Set<Effect> = emptySet()

    sealed class Action {
        data class InitMechanism(val mechanismType: MechanismTypeClass) : Action()

        object GetTaskList : Action()
        data class GetTaskListComplete(val items: List<TaskInfo>) : Action()

        object Logout : Action()
        object LogoutComplete : Action()

        object StartMechanismService : Action()
        data class StartMechanismServiceComplete(val message: String) : Action()

        data class StartTask(val taskId: Int) : Action()
        data class StartTaskComplete(val message: String, val taskId: Int) : Action()

        data class StopTask(val taskId: Int) : Action()
        data class StopTaskComplete(val message: String) : Action()

        data class Error(val error: Throwable) : Action()
    }

    sealed class Effect {
        object Logout : Effect()

        object GetTaskList : Effect()
        object StartMechanismService : Effect()
        data class StartSimpleTask(val taskId: Int, val request: StartTaskRequest) : Effect()

        data class StopCombinedTask(val taskId: Int, val request: StopTaskRequest) : Effect()

        data class DispatchEvent(val event: Event) : Effect()
        data class DispatchMessage(val message: String) : Effect()
    }

    sealed class Event {
        object Logout : Event()

        data class StartMechanismServiceComplete(val message: String) : Event()
        data class StartSimpleTaskComplete(val message: String, val taskInfo: TaskInfo) : Event()

        data class Error(val error: Throwable) : Event()
    }

}
