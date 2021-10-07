package com.vps.android.presentation.select_task.feature

import com.vps.android.core.network.base.RequestResult
import com.vps.android.interactors.auth.AuthInteractor
import com.vps.android.interactors.mechanism.MechanismInteractor
import com.vps.android.interactors.task.TaskInteractor
import com.vps.android.presentation.base.EffHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.withContext

class SelectTaskEffectHandler(
    private val taskInteractor: TaskInteractor,
    private val authInteractor: AuthInteractor,
    private val mechanismInteractor: MechanismInteractor,
    private val events: Channel<SelectTaskFeature.Event>,
    private val messages: Channel<String>,
) : EffHandler<SelectTaskFeature.Effect, SelectTaskFeature.Action> {

    override suspend fun handle(
        effect: SelectTaskFeature.Effect,
        commit: (SelectTaskFeature.Action) -> Unit,
    ) {
        when (effect) {
            is SelectTaskFeature.Effect.Logout -> {
                withContext(Dispatchers.IO) {
                    val result = authInteractor.logout()
                    when (result) {
                        is RequestResult.Success -> {
                            commit(SelectTaskFeature.Action.LogoutComplete)
                        }
                        else -> {
                            commit(SelectTaskFeature.Action.Error(Throwable(result.toString())))
                        }
                    }
                }
            }

            is SelectTaskFeature.Effect.GetTaskTypes -> {
                withContext(Dispatchers.IO) {
                    val result = taskInteractor.getTaskTypesList()
                    commit(SelectTaskFeature.Action.GetTaskTypesComplete(result))
                }
            }
            is SelectTaskFeature.Effect.GetTaskLoadingPlaces -> {
                withContext(Dispatchers.IO) {
                    val result = taskInteractor.getPlacesList()
                    commit(SelectTaskFeature.Action.GetTaskLoadingPlacesComplete(result))
                }
            }
            is SelectTaskFeature.Effect.GetTaskGoods -> {
                withContext(Dispatchers.IO) {
                    val result = taskInteractor.getGoodsList()
                    commit(SelectTaskFeature.Action.GetTaskGoodsComplete(result))
                }
            }
            is SelectTaskFeature.Effect.GetTaskTech -> {
                withContext(Dispatchers.IO) {
                    val result = mechanismInteractor.getCombinedMechanismList()
                    commit(SelectTaskFeature.Action.GetTaskTechComplete(result))
                }
            }


            is SelectTaskFeature.Effect.DispatchEvent -> {
                events.send(effect.event)
            }
            is SelectTaskFeature.Effect.DispatchMessage -> {
                messages.send(effect.message)
            }
        }
    }

}
