package com.vps.android.presentation.mechanism.feature

import com.vps.android.domain.mechanism.MechanismItem
import com.vps.android.presentation.base.BaseFeature

class MechanismFeature : BaseFeature<MechanismState, MechanismFeature.Action, MechanismFeature.Effect>() {

    override fun initialState(): MechanismState = MechanismState()
    override fun initialEffs(): Set<Effect> = emptySet()

    sealed class Action {
        data class GetMechanismTypeList(val typeId: Int) : Action()
        data class GetMechanismTypeListComplete(val list: List<MechanismItem>) : Action()

        data class SelectMechanism(val item: MechanismItem) : Action()
        data class SelectMechanismComplete(val item: MechanismItem) : Action()

        object Logout : Action()
        object LogoutComplete : Action()

        data class Error(val error: Throwable) : Action()
    }

    sealed class Effect {
        data class GetMechanismTypeList(val typeId: Int) : Effect()
        data class SelectMechanism(val item: MechanismItem) : Effect()

        object Logout : Effect()

        data class DispatchEvent(val event: Event) : Effect()
        data class DispatchMessage(val message: String) : Effect()
    }

    sealed class Event {
        object MechanismNotInService : Event()
        object MechanismInService : Event()
        object Logout : Event()

        data class Error(val error: Throwable) : Event()
    }

}
