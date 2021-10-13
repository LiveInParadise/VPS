package com.vps.android.presentation.main.feature

import android.os.Parcelable
import com.vps.android.core.ext.toRequestFormat
import com.vps.android.domain.mechanism.MechanismTypeClass
import com.vps.android.domain.task.TaskInfo
import com.vps.android.interactors.task.request.StartTaskRequest
import com.vps.android.interactors.task.request.StopTaskRequest
import com.vps.android.presentation.base.IViewModelState
import com.vps.android.presentation.base.Reducer
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class MainState(
    val taskItems: List<TaskInfo> = listOf(),
    val mechanismTypeClass: MechanismTypeClass? = null,
    val isLoading: Boolean = false,
) : Parcelable, Reducer<MainState, MainFeature.Action, MainFeature.Effect>, IViewModelState {

    override suspend fun reduce(action: MainFeature.Action): Pair<MainState, Set<MainFeature.Effect>> =
        when (action) {
            is MainFeature.Action.InitMechanism -> {
                copy(mechanismTypeClass = action.mechanismType) to setOf()
            }
            is MainFeature.Action.SendTotalDistance -> {
                copy() to setOf(MainFeature.Effect.SendTotalDistance)
            }

            is MainFeature.Action.GetTaskList -> {
                if (taskItems.none { it.isActive() }) {
                    copy(isLoading = true) to setOf(MainFeature.Effect.GetTaskList)
                } else {
                    copy() to setOf()
                }
            }
            is MainFeature.Action.GetTaskListForce -> {
                copy() to setOf(MainFeature.Effect.GetTaskList)
            }
            is MainFeature.Action.GetTaskListComplete -> {
                copy(isLoading = false, taskItems = action.items) to setOf()
            }

            is MainFeature.Action.StartMechanismService -> {
                if (taskItems.none { it.isActive() }) {
                    copy() to setOf(MainFeature.Effect.StartMechanismService)
                }else{
                    copy() to setOf(MainFeature.Effect.DispatchEvent(MainFeature.Event.StartServiceWithActiveTaskError))
                }
            }
            is MainFeature.Action.StartMechanismServiceComplete -> {
                copy() to setOf(MainFeature.Effect.DispatchEvent(MainFeature.Event.StartMechanismServiceComplete(action.message)))
            }

            is MainFeature.Action.StartTask -> {
                if (taskItems.none { it.isActive() }) {
                    val request = StartTaskRequest(Date().toRequestFormat())
                    copy() to setOf(MainFeature.Effect.StartSimpleTask(action.taskId, request))
                } else {
                    copy() to setOf(MainFeature.Effect.DispatchEvent(MainFeature.Event.StartSecondTaskError))
                }
            }
            is MainFeature.Action.StartTaskComplete -> {
                taskItems.firstOrNull { it.id == action.taskId }?.let {
                    if (mechanismTypeClass == MechanismTypeClass.SIMPLE) {
                        copy() to setOf(MainFeature.Effect.DispatchEvent(MainFeature.Event.StartSimpleTaskComplete(action.message, it)))
                    } else {
                        copy() to setOf(MainFeature.Effect.DispatchEvent(MainFeature.Event.StartCombinedTaskComplete(action.message)))
                    }
                } ?: run {
                    copy() to setOf()
                }
            }

            is MainFeature.Action.StopTask -> {
                if (action.taskInfo.isDelegated() && action.taskInfo.unloadingPlaceId == null) {
                    copy() to setOf(MainFeature.Effect.DispatchEvent(MainFeature.Event.StopTaskWithoutUnloadingPlaceError))
                } else {
                    val request = StopTaskRequest(Date().toRequestFormat())
                    copy() to setOf(MainFeature.Effect.StopCombinedTask(action.taskInfo.id, request))
                }
            }
            is MainFeature.Action.StopTaskComplete -> {
                copy(isLoading = true) to setOf(MainFeature.Effect.GetTaskList)
            }

            is MainFeature.Action.Logout -> {
                if (taskItems.none { it.isActive() }) {
                    copy() to setOf(MainFeature.Effect.Logout)
                } else {
                    copy() to setOf(MainFeature.Effect.DispatchEvent(MainFeature.Event.LogoutWithActiveTaskError))
                }
            }
            is MainFeature.Action.LogoutComplete -> {
                copy() to setOf(MainFeature.Effect.DispatchEvent(MainFeature.Event.Logout))
            }

            is MainFeature.Action.Error -> {
                val error = action.error
                copy(isLoading = false) to setOf(MainFeature.Effect.DispatchEvent(MainFeature.Event.Error(error)))
            }
        }

}
