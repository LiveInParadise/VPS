package com.vps.android.presentation.mechanism_type.feature

import com.vps.android.domain.mechanism.MechanismType
import com.vps.android.presentation.base.BaseFeature

class MechanismTypeFeature : BaseFeature<MechanismTypeState, MechanismTypeFeature.Action, MechanismTypeFeature.Effect>() {

    override fun initialState(): MechanismTypeState = MechanismTypeState()
    override fun initialEffs(): Set<Effect> = emptySet()

    sealed class Action {
        object GetMechanismTypes : Action()
        data class GetMechanismTypesComplete(val list: List<MechanismType>) : Action()

        object Logout : Action()
        object LogoutComplete : Action()

        data class Error(val error: Throwable) : Action()
    }

    sealed class Effect {
        object GetMechanismTypes : Effect()

        object Logout : Effect()

        data class DispatchEvent(val event: Event) : Effect()
        data class DispatchMessage(val message: String) : Effect()
    }

    sealed class Event {
        object Logout : Event()

        data class Error(val error: Throwable) : Event()
    }

}
