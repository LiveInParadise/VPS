package com.vps.android.presentation.task.feature

import com.vps.android.core.network.base.RequestResult
import com.vps.android.interactors.auth.AuthInteractor
import com.vps.android.interactors.task.TaskInteractor
import com.vps.android.presentation.base.EffHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.withContext

class AddTaskEffectHandler(
    private val authInteractor: AuthInteractor,
    private val taskInteractor: TaskInteractor,
    private val events: Channel<AddTaskFeature.Event>,
    private val messages: Channel<String>,
) : EffHandler<AddTaskFeature.Effect, AddTaskFeature.Action> {

    override suspend fun handle(
        effect: AddTaskFeature.Effect,
        commit: (AddTaskFeature.Action) -> Unit,
    ) {
        when (effect) {
            is AddTaskFeature.Effect.Logout -> {
                withContext(Dispatchers.IO) {
                    val result = authInteractor.logout()
                    when (result) {
                        is RequestResult.Success -> {
                            commit(AddTaskFeature.Action.LogoutComplete)
                        }
                        else -> {
                            commit(AddTaskFeature.Action.Error(Throwable(result.toString())))
                        }
                    }
                }
            }
            is AddTaskFeature.Effect.CreateTask -> {
                withContext(Dispatchers.IO) {
                    val result = taskInteractor.createTask(effect.request)
                    when (result) {
                        is RequestResult.Success -> {
                            commit(AddTaskFeature.Action.CreateTaskComplete(result.data))
                        }
                        else -> {
                            commit(AddTaskFeature.Action.Error(Throwable(result.toString())))
                        }
                    }
                }
            }

            is AddTaskFeature.Effect.DispatchEvent -> {
                events.send(effect.event)
            }
            is AddTaskFeature.Effect.DispatchMessage -> {
                messages.send(effect.message)
            }
        }
    }

}
