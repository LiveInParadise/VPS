package com.vps.android.presentation.auth.feature

import android.os.Parcelable
import com.vps.android.presentation.base.IViewModelState
import com.vps.android.presentation.base.Reducer
import kotlinx.parcelize.Parcelize

@Parcelize
data class AuthState(
    val phoneNumber: String = "",
    val isLoading: Boolean = false,
    val isUpdated: Boolean = false,
) : Parcelable, Reducer<AuthState, AuthFeature.Action, AuthFeature.Effect>, IViewModelState {

    override suspend fun reduce(action: AuthFeature.Action): Pair<AuthState, Set<AuthFeature.Effect>> =
        when (action) {
            is AuthFeature.Action.Auth -> {
                copy(isLoading = true) to setOf(AuthFeature.Effect.Auth(action.number, action.password))
            }

            is AuthFeature.Action.Error -> {
                val error = action.error
                copy(isLoading = false) to setOf(AuthFeature.Effect.DispatchEvent(AuthFeature.Event.Error(error)))
            }
        }

}
