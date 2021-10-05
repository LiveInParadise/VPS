package com.vps.android.presentation.mechanism_type.feature

import com.vps.android.core.network.base.RequestResult
import com.vps.android.interactors.auth.AuthInteractor
import com.vps.android.interactors.mechanism.MechanismInteractor
import com.vps.android.presentation.base.EffHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.withContext

class MechanismTypeEffectHandler(
    private val mechanismInteractor: MechanismInteractor,
    private val authInteractor: AuthInteractor,
    private val events: Channel<MechanismTypeFeature.Event>,
    private val messages: Channel<String>,
) : EffHandler<MechanismTypeFeature.Effect, MechanismTypeFeature.Action> {

    override suspend fun handle(
        effect: MechanismTypeFeature.Effect,
        commit: (MechanismTypeFeature.Action) -> Unit,
    ) {
        when (effect) {
            is MechanismTypeFeature.Effect.GetMechanismTypes -> {
                withContext(Dispatchers.IO) {
                    val result = mechanismInteractor.getMechanismTypes()
                    commit(MechanismTypeFeature.Action.GetMechanismTypesComplete(result))
                }
            }
            is MechanismTypeFeature.Effect.Logout -> {
                withContext(Dispatchers.IO) {
                    val result = authInteractor.logout()
                    when (result) {
                        is RequestResult.Success -> {
                            commit(MechanismTypeFeature.Action.LogoutComplete)
                        }
                        else -> {
                            commit(MechanismTypeFeature.Action.Error(Throwable(result.toString())))
                        }
                    }
                }
            }

            is MechanismTypeFeature.Effect.DispatchEvent -> {
                events.send(effect.event)
            }
            is MechanismTypeFeature.Effect.DispatchMessage -> {
                messages.send(effect.message)
            }
        }
    }

}
