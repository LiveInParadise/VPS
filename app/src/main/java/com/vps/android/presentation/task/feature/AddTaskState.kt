package com.vps.android.presentation.task.feature

import android.os.Parcelable
import com.vps.android.domain.mechanism.MechanismItem
import com.vps.android.domain.mechanism.MechanismTypeClass
import com.vps.android.domain.task.GoodItem
import com.vps.android.domain.task.PlaceItem
import com.vps.android.domain.task.TaskTypeClass
import com.vps.android.domain.task.TaskTypeItem
import com.vps.android.interactors.task.request.CreateTaskRequest
import com.vps.android.interactors.task.request.UpdateTaskRequest
import com.vps.android.presentation.base.IViewModelState
import com.vps.android.presentation.base.Reducer
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddTaskState(
    val mechanismType: MechanismTypeClass = MechanismTypeClass.SIMPLE,
    val taskTypeClass: TaskTypeClass = TaskTypeClass.SIMPLE_NEW,
    val taskId: Int? = null,
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
                val spec = action.spec
                copy(
                    taskId = spec.taskId,
                    mechanismType = spec.mechanismTypeClass,
                    taskTypeClass = spec.taskTypeClass,
                    taskType = spec.taskType,
                    loadingPlace = spec.loadingPlace,
                    unloadingPlace = spec.unloadingPlace,
                    goodItem = spec.goodItem,
                    mechanismItemList = spec.mechanismItemList
                ) to setOf()
            }

            is AddTaskFeature.Action.Logout -> {
                when (taskTypeClass) {
                    TaskTypeClass.SIMPLE_EDIT_ACTIVE, TaskTypeClass.COMBINED_EDIT_ACTIVE -> {
                        copy() to setOf(AddTaskFeature.Effect.DispatchEvent(AddTaskFeature.Event.LogoutWithActiveTaskError))
                    }
                    else -> {
                        copy() to setOf(AddTaskFeature.Effect.Logout)
                    }
                }
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

            is AddTaskFeature.Action.CheckAndProcessTask -> {
                val isCorrect = when (mechanismType) {
                    MechanismTypeClass.SIMPLE -> {
                        taskType != null && loadingPlace != null
                    }
                    MechanismTypeClass.COMBINED -> {
                        taskType != null
                    }
                }
                if (isCorrect) {
                    if (taskId != null) {
                        val request = UpdateTaskRequest(
                            task_type_id = taskType?.id ?: -1,
                            loading_place_id = loadingPlace?.id,
                            unloading_place_id = unloadingPlace?.id,
                            goods_id = goodItem?.id,
                            selected_mechanisms = mechanismItemList?.map { it.id }
                        )
                        copy(createTaskLoading = true) to setOf(AddTaskFeature.Effect.UpdateTask(taskId, request))
                    } else {
                        val request = CreateTaskRequest(
                            task_type_id = taskType?.id ?: -1,
                            loading_place_id = loadingPlace?.id,
                            unloading_place_id = unloadingPlace?.id,
                            goods_id = goodItem?.id,
                            selected_mechanisms = mechanismItemList?.map { it.id }
                        )
                        copy(createTaskLoading = true) to setOf(AddTaskFeature.Effect.CreateTask(request))
                    }
                } else {
                    copy() to setOf(AddTaskFeature.Effect.DispatchEvent(AddTaskFeature.Event.ShowNotFillError))
                }
            }
            is AddTaskFeature.Action.ProcessTaskComplete -> {
                copy(createTaskLoading = false) to setOf(AddTaskFeature.Effect.DispatchEvent(AddTaskFeature.Event.CreateTaskComplete(action.taskInfo)))
            }

            is AddTaskFeature.Action.Error -> {
                val error = action.error
                copy(isLoading = false, createTaskLoading = false) to setOf(AddTaskFeature.Effect.DispatchEvent(AddTaskFeature.Event.Error(error)))
            }
        }

}
