package com.vps.android.presentation.auth.feature

import android.os.Parcelable
import com.vps.android.presentation.base.IViewModelState
import com.vps.android.presentation.base.Reducer
import kotlinx.parcelize.Parcelize

@Parcelize
data class AuthState(
    val phoneNumber: String = "",
    val isKeypadBlocked: Boolean = false,
    val showError: Boolean = false,
    val errorMessage: String? = null,
    val isLoading: Boolean = false,
) : Parcelable, Reducer<AuthState, AuthFeature.Action, AuthFeature.Effect>, IViewModelState {

    override suspend fun reduce(action: AuthFeature.Action): Pair<AuthState, Set<AuthFeature.Effect>> =
        when (action) {
            is AuthFeature.Action.Auth -> {
                copy(isLoading = true, isKeypadBlocked = true, showError = false) to setOf(AuthFeature.Effect.Auth(action.pinCode))
            }
            is AuthFeature.Action.AuthError -> {
                copy(isLoading = false, isKeypadBlocked = false, showError = true, errorMessage = action.message) to setOf()
            }
            is AuthFeature.Action.AuthComplete -> {
                copy(isLoading = false, isKeypadBlocked = false, showError = false) to setOf(AuthFeature.Effect.DispatchEvent(AuthFeature.Event.AuthSuccess))
            }

            is AuthFeature.Action.Error -> {
                val error = action.error
                copy(isLoading = false) to setOf(AuthFeature.Effect.DispatchEvent(AuthFeature.Event.Error(error)))
            }
        }

}
