package com.vps.android.presentation.base

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.*
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import com.vps.android.R
import com.vps.android.core.dispatchers.DispatchersProvider
import com.vps.android.core.network.base.RequestResult
import com.vps.android.core.network.errors.ApiError
import com.vps.android.core.utils.Notify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel : ViewModel(), KoinComponent, CoroutineScope {

    private val dispatchers: DispatchersProvider by inject()
    protected val context: Context by inject()
    private val parentJob = SupervisorJob()

    override val coroutineContext: CoroutineContext = dispatchers.default() + parentJob

    private val notifications = MutableLiveData<Event<Notify>>()

    private val navigation = MutableLiveData<Event<NavigationCommand>>()

    private val permissions = MutableLiveData<Event<List<String>>>()

    override fun onCleared() {
        parentJob.cancelChildren()
    }

    // контекст слушает и показывает
    fun observeNotifications(owner: LifecycleOwner, onNotify: (notification: Notify) -> Unit) {
        notifications.observe(owner, EventObserver { onNotify(it) })
    }

    fun observeNavigation(owner: LifecycleOwner, onNavigate: (command: NavigationCommand) -> Unit) {
        navigation.observe(owner, EventObserver { onNavigate(it) })
    }

    // navigation with commands
    open fun navigateBack() {
        navigate(NavigationCommand.Back)
    }

    fun notify(content: Notify) {
        notifications.value = Event(content)
    }

    open fun navigate(command: NavigationCommand) {
        navigation.value = Event(command)
    }

    fun requestPermissions(requestedPermissions: List<String>) {
        permissions.value = Event(requestedPermissions)
    }

    fun observePermissions(owner: LifecycleOwner, handle: (List<String>) -> Unit) {
        permissions.observe(owner, EventObserver { handle(it) })
    }

    suspend fun runOnUi(block: suspend () -> Unit) =
        withContext(this.dispatchers.main()) {
            block()
        }

    suspend inline fun <T> RequestResult<T>.handleResult(noinline block: suspend (RequestResult.Success<T>) -> Unit) {
        runOnUi {
            when (val result = this@handleResult) {
                is RequestResult.Success -> block(result)
                is RequestResult.Error -> mapAndShowError(result.error)
            }
        }
    }

    suspend inline fun <T> RequestResult<T>.handleResultWithError(
        noinline resultBlock: suspend (RequestResult.Success<T>) -> Unit,
        noinline errorBlock: suspend (Throwable) -> Unit,
    ) = runOnUi {
        when (val result = this@handleResultWithError) {
            is RequestResult.Success -> resultBlock(result)
            is RequestResult.Error -> errorBlock(result.error)
        }
    }

    open fun mapAndShowError(error: Throwable) {
        notify(Notify.Error(mapError(error)))
    }

    open fun mapError(error: Throwable): String =
        context.getString(
            when (error) {
                is ApiError.GeneralError -> R.string.error_api_default
                is ApiError.NoDataError -> R.string.error_api_default
                is ApiError.BreakDataError -> R.string.error_api_break_date
                is ApiError.TokenError -> R.string.error_api_token_error
                is ApiError.UnknownError -> R.string.error_api_default
                else -> R.string.error_api_default
            }
        )
}

class Event<out E>(private val content: E, private var consumeHandler: (() -> Unit)? = null) {

    var isConsumed = false

    fun consumeIfNotHandled(): E? {
        return if (isConsumed) null
        else {
            isConsumed = true
            consumeHandler?.invoke()
            consumeHandler = null
            content
        }
    }

    fun peekIfNotHandled(): E? {
        return if (isConsumed) null
        else {
            content
        }
    }

    fun peek(): E = content
}

class EventObserver<E>(private val onEventUnhandledContent: (E) -> Unit) : Observer<Event<E>> {

    override fun onChanged(event: Event<E>?) {
        event?.consumeIfNotHandled()?.let {
            onEventUnhandledContent(it)
        }
    }
}

sealed class NavigationCommand {

    data class To(
        val destination: Int,
        val args: Bundle? = null,
        val options: NavOptions? = null,
        val extras: Navigator.Extras? = null,
    ) : NavigationCommand()

    data class Dir(
        val directions: NavDirections,
        val options: NavOptions? = null,
    ) : NavigationCommand()

    data class DirForward(
        val directions: NavDirections,
        val forwardDirections: NavDirections,
        val options: NavOptions? = null,
    ) : NavigationCommand()

    object Logout : NavigationCommand()

    object Back : NavigationCommand()
}
