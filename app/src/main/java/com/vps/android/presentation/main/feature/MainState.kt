package com.vps.android.presentation.main.feature

import android.os.Parcelable
import com.vps.android.domain.mechanism.MechanismItem
import com.vps.android.presentation.base.IViewModelState
import com.vps.android.presentation.base.Reducer
import kotlinx.parcelize.Parcelize

@Parcelize
data class MainState(
    val list: List<MechanismItem> = listOf(),
    val isLoading: Boolean = false,
) : Parcelable, Reducer<MainState, MainFeature.Action, MainFeature.Effect>, IViewModelState {

    override suspend fun reduce(action: MainFeature.Action): Pair<MainState, Set<MainFeature.Effect>> =
        when (action) {
            is MainFeature.Action.Logout -> {
                copy() to setOf(MainFeature.Effect.Logout)
            }
            is MainFeature.Action.LogoutComplete -> {
                copy() to setOf(MainFeature.Effect.DispatchEvent(MainFeature.Event.Logout))
            }

            is MainFeature.Action.StartMechanismService -> {
                copy() to setOf(MainFeature.Effect.StartMechanismService)
            }
            is MainFeature.Action.StartMechanismServiceComplete -> {
                copy() to setOf(MainFeature.Effect.DispatchEvent(MainFeature.Event.StartMechanismServiceComplete))
            }

            is MainFeature.Action.Error -> {
                val error = action.error
                copy(isLoading = false) to setOf(MainFeature.Effect.DispatchEvent(MainFeature.Event.Error(error)))
            }
        }

}
