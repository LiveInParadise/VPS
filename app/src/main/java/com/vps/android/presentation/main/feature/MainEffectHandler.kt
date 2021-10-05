package com.vps.android.presentation.main.feature

import com.vps.android.core.network.base.RequestResult
import com.vps.android.interactors.auth.AuthInteractor
import com.vps.android.interactors.mechanism.MechanismInteractor
import com.vps.android.presentation.base.EffHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.withContext

class MainEffectHandler(
    private val mechanismInteractor: MechanismInteractor,
    private val authInteractor: AuthInteractor,
    private val events: Channel<MainFeature.Event>,
    private val messages: Channel<String>,
) : EffHandler<MainFeature.Effect, MainFeature.Action> {

    override suspend fun handle(
        effect: MainFeature.Effect,
        commit: (MainFeature.Action) -> Unit,
    ) {
        when (effect) {
            is MainFeature.Effect.Logout -> {
                withContext(Dispatchers.IO) {
                    val result = authInteractor.logout()
                    when (result) {
                        is RequestResult.Success -> {
                            commit(MainFeature.Action.LogoutComplete)
                        }
                        else -> {
                            commit(MainFeature.Action.Error(Throwable(result.toString())))
                        }
                    }
                }
            }
            is MainFeature.Effect.StartMechanismService -> {
                withContext(Dispatchers.IO) {
                    val result = mechanismInteractor.startMechanismService()
                    when (result) {
                        is RequestResult.Success -> {
                            commit(MainFeature.Action.StartMechanismServiceComplete)
                        }
                        else -> {
                            commit(MainFeature.Action.Error(Throwable(result.toString())))
                        }
                    }
                }
            }

            is MainFeature.Effect.DispatchEvent -> {
                events.send(effect.event)
            }
            is MainFeature.Effect.DispatchMessage -> {
                messages.send(effect.message)
            }
        }
    }

}
