package com.vps.android.presentation.task.feature

import com.vps.android.domain.mechanism.MechanismItem
import com.vps.android.domain.task.GoodItem
import com.vps.android.domain.task.PlaceItem
import com.vps.android.domain.task.TaskTypeItem
import com.vps.android.interactors.task.request.CreateTaskRequest
import com.vps.android.presentation.base.BaseFeature
import com.vps.android.presentation.task.AddTaskSpec

class AddTaskFeature : BaseFeature<AddTaskState, AddTaskFeature.Action, AddTaskFeature.Effect>() {

    override fun initialState(): AddTaskState = AddTaskState()
    override fun initialEffs(): Set<Effect> = emptySet()

    sealed class Action {
        data class InitData(val spec: AddTaskSpec) : Action()

        object Logout : Action()
        object LogoutComplete : Action()

        data class SaveTaskType(val taskType: TaskTypeItem) : Action()
        data class SaveLoadingPlace(val placeItem: PlaceItem) : Action()
        data class SaveUnloadingPlace(val placeItem: PlaceItem) : Action()
        data class SaveGoodItem(val item: GoodItem) : Action()
        data class SaveMechanismList(val items: List<MechanismItem>) : Action()

        object CheckAndCreateTask : Action()
        data class CreateTaskComplete(val message: String) : Action()

        data class Error(val error: Throwable) : Action()
    }

    sealed class Effect {
        object Logout : Effect()

        data class CreateTask(val request: CreateTaskRequest) : Effect()

        data class DispatchEvent(val event: Event) : Effect()
        data class DispatchMessage(val message: String) : Effect()
    }

    sealed class Event {
        object Logout : Event()

        object ShowNotFillError : Event()

        data class CreateTaskComplete(val message: String) : Event()

        data class Error(val error: Throwable) : Event()
    }

}
