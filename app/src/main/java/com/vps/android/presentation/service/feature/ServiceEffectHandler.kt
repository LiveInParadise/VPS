package com.vps.android.presentation.service.feature

import com.vps.android.core.network.base.RequestResult
import com.vps.android.interactors.auth.AuthInteractor
import com.vps.android.interactors.mechanism.MechanismInteractor
import com.vps.android.presentation.base.EffHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.withContext

class ServiceEffectHandler(
    private val mechanismInteractor: MechanismInteractor,
    private val authInteractor: AuthInteractor,
    private val events: Channel<ServiceFeature.Event>,
    private val messages: Channel<String>,
) : EffHandler<ServiceFeature.Effect, ServiceFeature.Action> {

    override suspend fun handle(
        effect: ServiceFeature.Effect,
        commit: (ServiceFeature.Action) -> Unit,
    ) {
        when (effect) {
            is ServiceFeature.Effect.Logout -> {
                withContext(Dispatchers.IO) {
                    val result = authInteractor.logout()
                    when (result) {
                        is RequestResult.Success -> {
                            commit(ServiceFeature.Action.LogoutComplete)
                        }
                        else -> {
                            commit(ServiceFeature.Action.Error(Throwable(result.toString())))
                        }
                    }
                }
            }
            is ServiceFeature.Effect.StopMechanismService -> {
                withContext(Dispatchers.IO) {
                    val result = mechanismInteractor.stopMechanismService()
                    when (result) {
                        is RequestResult.Success -> {
                            commit(ServiceFeature.Action.StopMechanismServiceComplete(result.data))
                        }
                        else -> {
                            commit(ServiceFeature.Action.Error(Throwable(result.toString())))
                        }
                    }
                }
            }

            is ServiceFeature.Effect.DispatchEvent -> {
                events.send(effect.event)
            }
            is ServiceFeature.Effect.DispatchMessage -> {
                messages.send(effect.message)
            }
        }
    }

}
