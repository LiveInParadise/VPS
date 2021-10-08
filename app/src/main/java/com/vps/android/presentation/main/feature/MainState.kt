package com.vps.android.presentation.main.feature

import android.os.Parcelable
import com.vps.android.domain.mechanism.MechanismTypeClass
import com.vps.android.domain.task.TaskInfo
import com.vps.android.presentation.base.IViewModelState
import com.vps.android.presentation.base.Reducer
import kotlinx.parcelize.Parcelize

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
            is MainFeature.Action.GetTaskList -> {
                copy(isLoading = true) to setOf(MainFeature.Effect.GetTaskList)
            }
            is MainFeature.Action.GetTaskListComplete -> {
                copy(isLoading = false, taskItems = action.items) to setOf()
            }

            is MainFeature.Action.StartMechanismService -> {
                copy() to setOf(MainFeature.Effect.StartMechanismService)
            }
            is MainFeature.Action.StartMechanismServiceComplete -> {
                copy() to setOf(MainFeature.Effect.DispatchEvent(MainFeature.Event.StartMechanismServiceComplete(action.message)))
            }

            is MainFeature.Action.Logout -> {
                copy() to setOf(MainFeature.Effect.Logout)
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
