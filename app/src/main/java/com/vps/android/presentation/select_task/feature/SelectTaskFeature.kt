package com.vps.android.presentation.select_task.feature

import com.vps.android.domain.mechanism.MechanismItem
import com.vps.android.domain.task.FullGoodItem
import com.vps.android.domain.task.PlaceItem
import com.vps.android.domain.task.TaskTypeItem
import com.vps.android.presentation.base.BaseFeature

class SelectTaskFeature : BaseFeature<SelectTaskState, SelectTaskFeature.Action, SelectTaskFeature.Effect>() {

    override fun initialState(): SelectTaskState = SelectTaskState()
    override fun initialEffs(): Set<Effect> = emptySet()

    sealed class Action {
        object Logout : Action()
        object LogoutComplete : Action()

        object GetTaskTypes : Action()
        data class GetTaskTypesComplete(val items: List<TaskTypeItem>) : Action()

        object GetTaskLoadingPlaces : Action()
        data class GetTaskLoadingPlacesComplete(val items: List<PlaceItem>) : Action()

        object GetTaskGoods : Action()
        data class GetTaskGoodsComplete(val items: List<FullGoodItem>) : Action()

        object GetTaskTech : Action()
        data class GetTaskTechComplete(val items: List<MechanismItem>) : Action()

        data class Error(val error: Throwable) : Action()
    }

    sealed class Effect {
        object Logout : Effect()

        object GetTaskTypes : Effect()
        object GetTaskLoadingPlaces : Effect()
        object GetTaskGoods : Effect()
        object GetTaskTech : Effect()

        data class DispatchEvent(val event: Event) : Effect()
        data class DispatchMessage(val message: String) : Effect()
    }

    sealed class Event {
        object Logout : Event()

        data class Error(val error: Throwable) : Event()
    }

}
