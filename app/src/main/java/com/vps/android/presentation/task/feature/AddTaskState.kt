package com.vps.android.presentation.task.feature

import android.os.Parcelable
import com.vps.android.domain.mechanism.MechanismItem
import com.vps.android.domain.mechanism.MechanismTypeClass
import com.vps.android.domain.task.GoodItem
import com.vps.android.domain.task.PlaceItem
import com.vps.android.domain.task.TaskTypeItem
import com.vps.android.interactors.task.request.CreateTaskRequest
import com.vps.android.presentation.base.IViewModelState
import com.vps.android.presentation.base.Reducer
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddTaskState(
    val mechanismType: MechanismTypeClass? = MechanismTypeClass.SIMPLE,
    val taskType: TaskTypeItem? = null,
    val loadingPlace: PlaceItem? = null,
    val unloadingPlace: PlaceItem? = null,
    val goodItem: GoodItem? = null,
    val mechanismItemList: List<MechanismItem>? = null,
    val createTaskLoading: Boolean = false,
    val isLoading: Boolean = false,
) : Parcelable, Reducer<AddTaskState, AddTaskFeature.Action, AddTaskFeature.Effect>, IViewModelState {

    override suspend fun reduce(action: AddTaskFeature.Action): Pair<AddTaskState, Set<AddTaskFeature.Effect>> =
        when (action) {
            is AddTaskFeature.Action.InitData -> {
                copy(mechanismType = action.spec.mechanismTypeClass) to setOf()
            }

            is AddTaskFeature.Action.Logout -> {
                copy() to setOf(AddTaskFeature.Effect.Logout)
            }
            is AddTaskFeature.Action.LogoutComplete -> {
                copy() to setOf(AddTaskFeature.Effect.DispatchEvent(AddTaskFeature.Event.Logout))
            }

            is AddTaskFeature.Action.SaveTaskType -> {
                copy(taskType = action.taskType) to setOf()
            }
            is AddTaskFeature.Action.SaveLoadingPlace -> {
                copy(loadingPlace = action.placeItem) to setOf()
            }
            is AddTaskFeature.Action.SaveUnloadingPlace -> {
                copy(unloadingPlace = action.placeItem) to setOf()
            }
            is AddTaskFeature.Action.SaveGoodItem -> {
                copy(goodItem = action.item) to setOf()
            }
            is AddTaskFeature.Action.SaveMechanismList -> {
                copy(mechanismItemList = action.items) to setOf()
            }

            is AddTaskFeature.Action.CheckAndCreateTask -> {
                val isCorrect = when (mechanismType) {
                    MechanismTypeClass.SIMPLE -> {
                        taskType != null && loadingPlace != null && unloadingPlace != null && goodItem != null
                    }
                    MechanismTypeClass.COMBINED -> {
                        taskType != null
                    }
                    else -> false
                }
                if (isCorrect) {
                    val request = CreateTaskRequest(
                        task_type_id = taskType?.id ?: -1,
                        loading_place_id = loadingPlace?.id,
                        unloading_place_id = unloadingPlace?.id,
                        goods_id = goodItem?.id,
                        selected_mechanisms = mechanismItemList?.map { it.id }
                    )
                    copy(createTaskLoading = true) to setOf(AddTaskFeature.Effect.CreateTask(request))
                } else {
                    AddTaskFeature.Effect.DispatchEvent(AddTaskFeature.Event.ShowNotFillError)
                    copy() to setOf(AddTaskFeature.Effect.DispatchEvent(AddTaskFeature.Event.ShowNotFillError))
                }
            }
            is AddTaskFeature.Action.CreateTaskComplete -> {
                copy(createTaskLoading = false) to setOf(AddTaskFeature.Effect.DispatchEvent(AddTaskFeature.Event.CreateTaskComplete(action.message)))
            }

            is AddTaskFeature.Action.Error -> {
                val error = action.error
                copy(isLoading = false) to setOf(AddTaskFeature.Effect.DispatchEvent(AddTaskFeature.Event.Error(error)))
            }
        }

}
