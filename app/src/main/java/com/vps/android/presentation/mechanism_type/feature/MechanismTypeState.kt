package com.vps.android.presentation.mechanism_type.feature

import android.os.Parcelable
import com.vps.android.domain.mechanism.MechanismType
import com.vps.android.presentation.base.IViewModelState
import com.vps.android.presentation.base.Reducer
import kotlinx.parcelize.Parcelize

@Parcelize
data class MechanismTypeState(
    val list: List<MechanismType> = listOf(),
    val isLoading: Boolean = false,
) : Parcelable, Reducer<MechanismTypeState, MechanismTypeFeature.Action, MechanismTypeFeature.Effect>, IViewModelState {

    override suspend fun reduce(action: MechanismTypeFeature.Action): Pair<MechanismTypeState, Set<MechanismTypeFeature.Effect>> =
        when (action) {
            is MechanismTypeFeature.Action.GetMechanismTypes -> {
                copy(isLoading = true) to setOf(MechanismTypeFeature.Effect.GetMechanismTypes)
            }
            is MechanismTypeFeature.Action.GetMechanismTypesComplete -> {
                copy(isLoading = false, list = action.list) to setOf()
            }
            is MechanismTypeFeature.Action.Logout -> {
                copy() to setOf(MechanismTypeFeature.Effect.Logout)
            }
            is MechanismTypeFeature.Action.LogoutComplete -> {
                copy() to setOf(MechanismTypeFeature.Effect.DispatchEvent(MechanismTypeFeature.Event.Logout))
            }

            is MechanismTypeFeature.Action.Error -> {
                val error = action.error
                copy(isLoading = false) to setOf(MechanismTypeFeature.Effect.DispatchEvent(MechanismTypeFeature.Event.Error(error)))
            }
        }

}
