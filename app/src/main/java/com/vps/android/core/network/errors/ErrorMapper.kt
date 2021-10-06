package com.vps.android.core.network.errors

import com.vps.android.core.network.base.BaseResponseObj
import com.vps.android.interactors.mechanism.response.MechanismSelectResponse

interface ErrorMapper {

    fun <T, D : BaseResponseObj<T>> mapError(error: Throwable, data: D? = null): Throwable

    fun <T, D : MechanismSelectResponse<T>> mapMechanismError(error: Throwable, data: D? = null): Throwable
}
