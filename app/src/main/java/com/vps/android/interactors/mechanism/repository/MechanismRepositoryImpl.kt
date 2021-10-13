package com.vps.android.interactors.mechanism.repository

import com.google.gson.Gson
import com.vps.android.core.dispatchers.DispatchersProvider
import com.vps.android.core.local.PrefManager
import com.vps.android.core.network.base.BaseRequestResultHandler
import com.vps.android.core.network.base.RequestResult
import com.vps.android.core.network.errors.ErrorMapper
import com.vps.android.domain.mechanism.MechanismItem
import com.vps.android.domain.mechanism.MechanismType
import com.vps.android.interactors.mechanism.api.MechanismApi
import com.vps.android.interactors.mechanism.request.MechanismSelectRequest
import com.vps.android.interactors.mechanism.request.TotalDistanceRequest

class MechanismRepositoryImpl(
    dispatchersProvider: DispatchersProvider,
    errorMapper: ErrorMapper,
    gson: Gson,
    private val api: MechanismApi,
    private val pref: PrefManager,
) : MechanismRepository, BaseRequestResultHandler(dispatchersProvider, errorMapper, gson) {

    override suspend fun getMechanismTypes(): List<MechanismType> =
        callAndMapListBase {
            api.getMechanismTypes()
        }

    override suspend fun getMechanismListByType(typeId: Int): List<MechanismItem> =
        callAndMapListBase {
            api.getMechanismListByType(typeId)
        }

    override suspend fun selectMechanism(mechanismId: Int): RequestResult<MechanismItem> =
        callAndMapMechanism {
            val request = MechanismSelectRequest(mechanismId)
            api.selectMechanism(request)
        }

    override suspend fun getCombinedMechanismList(): List<MechanismItem> =
        callAndMapListBase {
            api.getCombinedMechanismList()
        }

    override suspend fun startMechanismService(): RequestResult<String> =
        call {
            api.startMechanismService()
        }

    override suspend fun stopMechanismService(): RequestResult<String> =
        call {
            api.stopMechanismService()
        }

    override suspend fun sendTotalDistance(distance: Double): RequestResult<String> =
        call {
            val request = TotalDistanceRequest(distance)
            api.sendTotalDistance(request)
        }
}
