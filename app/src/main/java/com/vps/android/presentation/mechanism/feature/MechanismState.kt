package com.vps.android.presentation.mechanism.feature

import android.os.Parcelable
import com.vps.android.domain.mechanism.MechanismItem
import com.vps.android.presentation.base.IViewModelState
import com.vps.android.presentation.base.Reducer
import kotlinx.parcelize.Parcelize

@Parcelize
data class MechanismState(
    val list: List<MechanismItem> = listOf(),
    val isLoading: Boolean = false,
) : Parcelable, Reducer<MechanismState, MechanismFeature.Action, MechanismFeature.Effect>, IViewModelState {

    override suspend fun reduce(action: MechanismFeature.Action): Pair<MechanismState, Set<MechanismFeature.Effect>> =
        when (action) {
            is MechanismFeature.Action.GetMechanismTypeList -> {
                copy(isLoading = true) to setOf(MechanismFeature.Effect.GetMechanismTypeList(action.typeId))
            }
            is MechanismFeature.Action.GetMechanismTypeListComplete -> {
                copy(isLoading = false, list = action.list) to setOf()
            }

            is MechanismFeature.Action.SelectMechanism -> {
                copy() to setOf(MechanismFeature.Effect.SelectMechanism(action.item))
            }
            is MechanismFeature.Action.SelectMechanismComplete -> {
                val event = if (action.item.inService()) {
                    MechanismFeature.Effect.DispatchEvent(MechanismFeature.Event.MechanismInService)
                } else {
                    MechanismFeature.Effect.DispatchEvent(MechanismFeature.Event.MechanismNotInService)
                }
                copy() to setOf(event)
            }

            is MechanismFeature.Action.Logout -> {
                copy() to setOf(MechanismFeature.Effect.Logout)
            }
            is MechanismFeature.Action.LogoutComplete -> {
                copy() to setOf(MechanismFeature.Effect.DispatchEvent(MechanismFeature.Event.Logout))
            }

            is MechanismFeature.Action.Error -> {
                val error = action.error
                copy(isLoading = false) to setOf(MechanismFeature.Effect.DispatchEvent(MechanismFeature.Event.Error(error)))
            }
        }

}
