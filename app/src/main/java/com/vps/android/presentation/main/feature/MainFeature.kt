package com.vps.android.presentation.main.feature

import com.vps.android.domain.mechanism.MechanismTypeClass
import com.vps.android.domain.task.TaskInfo
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

        data class Error(val error: Throwable) : Action()
    }

    sealed class Effect {
        object Logout : Effect()

        object GetTaskList : Effect()
        object StartMechanismService : Effect()

        data class DispatchEvent(val event: Event) : Effect()
        data class DispatchMessage(val message: String) : Effect()
    }

    sealed class Event {
        object Logout : Event()

        data class StartMechanismServiceComplete(val message: String) : Event()

        data class Error(val error: Throwable) : Event()
    }

}
