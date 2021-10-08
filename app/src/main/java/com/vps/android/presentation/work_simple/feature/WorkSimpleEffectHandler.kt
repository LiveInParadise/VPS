package com.vps.android.presentation.work_simple.feature

import com.vps.android.core.network.base.RequestResult
import com.vps.android.interactors.mechanism.MechanismInteractor
import com.vps.android.interactors.task.TaskInteractor
import com.vps.android.interactors.task.request.StopTaskRequest
import com.vps.android.presentation.base.EffHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class WorkSimpleEffectHandler(
    private val taskInteractor: TaskInteractor,
    private val mechanismInteractor: MechanismInteractor,
    private val events: Channel<WorkSimpleFeature.Event>,
    private val messages: Channel<String>,
) : EffHandler<WorkSimpleFeature.Effect, WorkSimpleFeature.Action> {

    override suspend fun handle(
        effect: WorkSimpleFeature.Effect,
        commit: (WorkSimpleFeature.Action) -> Unit,
    ) {
        when (effect) {
            is WorkSimpleFeature.Effect.StopTask -> {
                withContext(Dispatchers.IO) {
                    val currentDate = SimpleDateFormat("Y-M-d H:m:s").format(Date())
                    val request = StopTaskRequest(currentDate)
                    val result = taskInteractor.stopTask(effect.taskId, request)
                    when (result) {
                        is RequestResult.Success -> {
                            commit(WorkSimpleFeature.Action.StopTaskComplete(result.data))
                        }
                        else -> {
                            commit(WorkSimpleFeature.Action.Error(Throwable(result.toString())))
                        }
                    }
                }
            }
            is WorkSimpleFeature.Effect.SelectMechanism -> {
                withContext(Dispatchers.IO) {
                    val result = taskInteractor.selectMechanism(effect.mechanismId)
                    when (result) {
                        is RequestResult.Success -> {
                            commit(WorkSimpleFeature.Action.SelectMechanismComplete(effect.mechanismId, result.data))
                        }
                        is RequestResult.Error -> {
                            commit(WorkSimpleFeature.Action.SelectMechanismError(result.error.message ?: ""))
                        }
                    }
                }
            }

            is WorkSimpleFeature.Effect.DispatchEvent -> {
                events.send(effect.event)
            }
            is WorkSimpleFeature.Effect.DispatchMessage -> {
                messages.send(effect.message)
            }
        }
    }

}
