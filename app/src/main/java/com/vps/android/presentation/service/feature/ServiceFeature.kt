package com.vps.android.presentation.service.feature

import com.vps.android.presentation.base.BaseFeature

class ServiceFeature : BaseFeature<ServiceState, ServiceFeature.Action, ServiceFeature.Effect>() {

    override fun initialState(): ServiceState = ServiceState()
    override fun initialEffs(): Set<Effect> = emptySet()

    sealed class Action {
        object Logout : Action()
        object LogoutComplete : Action()

        object StopMechanismService : Action()
        object StopMechanismServiceComplete : Action()

        data class Error(val error: Throwable) : Action()
    }

    sealed class Effect {
        object Logout : Effect()

        object StopMechanismService : Effect()

        data class DispatchEvent(val event: Event) : Effect()
        data class DispatchMessage(val message: String) : Effect()
    }

    sealed class Event {
        object Logout : Event()

        object StopMechanismServiceComplete : Event()

        data class Error(val error: Throwable) : Event()
    }

}
