package com.vps.android.presentation.auth.feature

import com.vps.android.core.network.base.RequestResult
import com.vps.android.interactors.auth.AuthInteractor
import com.vps.android.presentation.base.EffHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.withContext

class AuthEffectHandler(
    private val authInteractor: AuthInteractor,
    private val events: Channel<AuthFeature.Event>,
    private val messages: Channel<String>,
) : EffHandler<AuthFeature.Effect, AuthFeature.Action> {

    override suspend fun handle(
        effect: AuthFeature.Effect,
        commit: (AuthFeature.Action) -> Unit,
    ) {
        when (effect) {
            is AuthFeature.Effect.Auth -> {
                withContext(Dispatchers.IO) {
                    val result = authInteractor.checkAuth()
                    when (result) {
                        is RequestResult.Success -> {
//                            commit(AuthFeature.Action.AuthComplete(effect.number, result.data))
                        }
                        is RequestResult.Error -> {
                            commit(AuthFeature.Action.Error(result.error))
                        }
                    }
                }
            }

            is AuthFeature.Effect.DispatchEvent -> {
                events.send(effect.event)
            }
            is AuthFeature.Effect.DispatchMessage -> {
                messages.send(effect.message)
            }
        }
    }

}
