package com.vps.android.presentation.service.feature

import android.os.Parcelable
import com.vps.android.domain.mechanism.MechanismItem
import com.vps.android.presentation.base.IViewModelState
import com.vps.android.presentation.base.Reducer
import kotlinx.parcelize.Parcelize

@Parcelize
data class ServiceState(
    val list: List<MechanismItem> = listOf(),
    val isLoading: Boolean = false,
) : Parcelable, Reducer<ServiceState, ServiceFeature.Action, ServiceFeature.Effect>, IViewModelState {

    override suspend fun reduce(action: ServiceFeature.Action): Pair<ServiceState, Set<ServiceFeature.Effect>> =
        when (action) {
            is ServiceFeature.Action.Logout -> {
                copy() to setOf(ServiceFeature.Effect.Logout)
            }
            is ServiceFeature.Action.LogoutComplete -> {
                copy() to setOf(ServiceFeature.Effect.DispatchEvent(ServiceFeature.Event.Logout))
            }

            is ServiceFeature.Action.StopMechanismService -> {
                copy() to setOf(ServiceFeature.Effect.StopMechanismService)
            }
            is ServiceFeature.Action.StopMechanismServiceComplete -> {
                copy() to setOf(ServiceFeature.Effect.DispatchEvent(ServiceFeature.Event.StopMechanismServiceComplete(action.message)))
            }

            is ServiceFeature.Action.Error -> {
                val error = action.error
                copy(isLoading = false) to setOf(ServiceFeature.Effect.DispatchEvent(ServiceFeature.Event.Error(error)))
            }
        }

}
