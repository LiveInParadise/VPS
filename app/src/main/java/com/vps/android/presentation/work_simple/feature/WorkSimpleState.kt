package com.vps.android.presentation.work_simple.feature

import android.os.Parcelable
import com.vps.android.domain.task.TaskInfo
import com.vps.android.presentation.base.IViewModelState
import com.vps.android.presentation.base.Reducer
import kotlinx.parcelize.Parcelize

@Parcelize
data class WorkSimpleState(
    val taskInfo: TaskInfo? = null,
    val showError: Boolean = false,
    val errorMessage: String? = null,
    val isLoading: Boolean = false,
) : Parcelable, Reducer<WorkSimpleState, WorkSimpleFeature.Action, WorkSimpleFeature.Effect>, IViewModelState {

    override suspend fun reduce(action: WorkSimpleFeature.Action): Pair<WorkSimpleState, Set<WorkSimpleFeature.Effect>> =
        when (action) {
            is WorkSimpleFeature.Action.InitTask -> {
                copy(taskInfo = action.taskInfo) to setOf()
            }

            is WorkSimpleFeature.Action.StopTask -> {
                copy(showError = false) to setOf(WorkSimpleFeature.Effect.StopTask(taskInfo?.id ?: -1))
            }
            is WorkSimpleFeature.Action.StopTaskComplete -> {
                copy(showError = false) to setOf(WorkSimpleFeature.Effect.DispatchEvent(WorkSimpleFeature.Event.StopTaskComplete(action.message)))
            }

            is WorkSimpleFeature.Action.SelectMechanism -> {
                copy(showError = false) to setOf(WorkSimpleFeature.Effect.SelectMechanism(action.mechanismId))
            }
            is WorkSimpleFeature.Action.SelectMechanismComplete -> {
                copy(showError = false) to setOf(WorkSimpleFeature.Effect.DispatchEvent(WorkSimpleFeature.Event.SelectMechanismSuccess(action.mechanismId, action.message)))
            }
            is WorkSimpleFeature.Action.SelectMechanismError -> {
                copy(showError = true, errorMessage = action.message) to setOf()
            }

            is WorkSimpleFeature.Action.Error -> {
                val error = action.error
                copy(isLoading = false) to setOf(WorkSimpleFeature.Effect.DispatchEvent(WorkSimpleFeature.Event.Error(error)))
            }
        }

}
