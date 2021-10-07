package com.vps.android.presentation.select_task.feature

import android.os.Parcelable
import com.vps.android.domain.mechanism.MechanismItem
import com.vps.android.domain.task.FullGoodItem
import com.vps.android.domain.task.PlaceItem
import com.vps.android.domain.task.TaskTypeItem
import com.vps.android.presentation.base.IViewModelState
import com.vps.android.presentation.base.Reducer
import kotlinx.parcelize.Parcelize

@Parcelize
data class SelectTaskState(
    val taskTypesList: List<TaskTypeItem> = listOf(),
    val taskPlacesList: List<PlaceItem> = listOf(),
    val taskGoodsList: List<FullGoodItem> = listOf(),
    val taskTechList: List<MechanismItem> = listOf(),
    val isLoading: Boolean = false,
) : Parcelable, Reducer<SelectTaskState, SelectTaskFeature.Action, SelectTaskFeature.Effect>, IViewModelState {

    override suspend fun reduce(action: SelectTaskFeature.Action): Pair<SelectTaskState, Set<SelectTaskFeature.Effect>> =
        when (action) {
            is SelectTaskFeature.Action.Logout -> {
                copy() to setOf(SelectTaskFeature.Effect.Logout)
            }
            is SelectTaskFeature.Action.LogoutComplete -> {
                copy() to setOf(SelectTaskFeature.Effect.DispatchEvent(SelectTaskFeature.Event.Logout))
            }

            is SelectTaskFeature.Action.GetTaskTypes -> {
                copy(isLoading = true) to setOf(SelectTaskFeature.Effect.GetTaskTypes)
            }
            is SelectTaskFeature.Action.GetTaskTypesComplete -> {
                copy(isLoading = false, taskTypesList = action.items) to setOf()
            }

            is SelectTaskFeature.Action.GetTaskLoadingPlaces -> {
                copy(isLoading = true) to setOf(SelectTaskFeature.Effect.GetTaskLoadingPlaces)
            }
            is SelectTaskFeature.Action.GetTaskLoadingPlacesComplete -> {
                copy(isLoading = false, taskPlacesList = action.items) to setOf()
            }

            is SelectTaskFeature.Action.GetTaskGoods -> {
                copy(isLoading = true) to setOf(SelectTaskFeature.Effect.GetTaskGoods)
            }
            is SelectTaskFeature.Action.GetTaskGoodsComplete -> {
                copy(isLoading = false, taskGoodsList = action.items) to setOf()
            }

            is SelectTaskFeature.Action.GetTaskTech -> {
                copy(isLoading = true) to setOf(SelectTaskFeature.Effect.GetTaskTech)
            }
            is SelectTaskFeature.Action.GetTaskTechComplete -> {
                copy(isLoading = false, taskTechList = action.items) to setOf()
            }

            is SelectTaskFeature.Action.Error -> {
                val error = action.error
                copy(isLoading = false) to setOf(SelectTaskFeature.Effect.DispatchEvent(SelectTaskFeature.Event.Error(error)))
            }
        }

}
