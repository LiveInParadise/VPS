package com.vps.android.presentation.mechanism.feature

import com.vps.android.core.network.base.RequestResult
import com.vps.android.interactors.auth.AuthInteractor
import com.vps.android.interactors.mechanism.MechanismInteractor
import com.vps.android.presentation.base.EffHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.withContext

class MechanismEffectHandler(
    private val mechanismInteractor: MechanismInteractor,
    private val authInteractor: AuthInteractor,
    private val events: Channel<MechanismFeature.Event>,
    private val messages: Channel<String>,
) : EffHandler<MechanismFeature.Effect, MechanismFeature.Action> {

    override suspend fun handle(
        effect: MechanismFeature.Effect,
        commit: (MechanismFeature.Action) -> Unit,
    ) {
        when (effect) {
            is MechanismFeature.Effect.GetCombinedMechanismList -> {
                withContext(Dispatchers.IO) {
                    val result = mechanismInteractor.getCombinedMechanismList()
                    commit(MechanismFeature.Action.GetCombinedMechanismListComplete(result))
                }
            }
            is MechanismFeature.Effect.Logout -> {
                withContext(Dispatchers.IO) {
                    val result = authInteractor.logout()
                    when (result) {
                        is RequestResult.Success -> {
                            commit(MechanismFeature.Action.LogoutComplete)
                        }
                        else -> {
                            commit(MechanismFeature.Action.Error(Throwable(result.toString())))
                        }
                    }
                }
            }

            is MechanismFeature.Effect.DispatchEvent -> {
                events.send(effect.event)
            }
            is MechanismFeature.Effect.DispatchMessage -> {
                messages.send(effect.message)
            }
        }
    }

}
