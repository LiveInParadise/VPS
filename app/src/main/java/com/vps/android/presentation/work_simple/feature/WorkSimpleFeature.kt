package com.vps.android.presentation.work_simple.feature

import com.vps.android.domain.task.TaskInfo
import com.vps.android.presentation.base.BaseFeature

class WorkSimpleFeature : BaseFeature<WorkSimpleState, WorkSimpleFeature.Action, WorkSimpleFeature.Effect>() {

    override fun initialState(): WorkSimpleState = WorkSimpleState()
    override fun initialEffs(): Set<Effect> = emptySet()

    sealed class Action {
        data class InitTask(val taskInfo: TaskInfo) : Action()

        object StopTask : Action()
        data class StopTaskComplete(val message: String) : Action()

        data class SelectMechanism(val mechanismId: Int) : Action()
        data class SelectMechanismComplete(val mechanismId: Int, val message: String) : Action()
        data class SelectMechanismError(val message: String) : Action()

        data class Error(val error: Throwable) : Action()
    }

    sealed class Effect {
        data class StopTask(val taskId: Int) : Effect()
        data class SelectMechanism(val mechanismId: Int) : Effect()

        data class DispatchEvent(val event: Event) : Effect()
        data class DispatchMessage(val message: String) : Effect()
    }

    sealed class Event {
        data class StopTaskComplete(val message: String) : Event()
        data class SelectMechanismSuccess(val mechanismId: Int, val message: String) : Event()

        data class Error(val error: Throwable) : Event()
    }

}
