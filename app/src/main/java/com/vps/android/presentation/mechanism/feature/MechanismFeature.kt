package com.vps.android.presentation.mechanism.feature

import com.vps.android.domain.mechanism.MechanismItem
import com.vps.android.presentation.base.BaseFeature

class MechanismFeature : BaseFeature<MechanismState, MechanismFeature.Action, MechanismFeature.Effect>() {

    override fun initialState(): MechanismState = MechanismState()
    override fun initialEffs(): Set<Effect> = emptySet()

    sealed class Action {
        object GetCombinedMechanismList : Action()
        data class GetCombinedMechanismListComplete(val list: List<MechanismItem>) : Action()

        object Logout : Action()
        object LogoutComplete : Action()

        data class Error(val error: Throwable) : Action()
    }

    sealed class Effect {
        object GetCombinedMechanismList : Effect()

        object Logout : Effect()

        data class DispatchEvent(val event: Event) : Effect()
        data class DispatchMessage(val message: String) : Effect()
    }

    sealed class Event {
        object Logout : Event()

        data class Error(val error: Throwable) : Event()
    }

}
