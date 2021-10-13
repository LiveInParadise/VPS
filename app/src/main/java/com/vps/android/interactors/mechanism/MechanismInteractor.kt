package com.vps.android.interactors.mechanism

import com.vps.android.domain.mechanism.MechanismType
import com.vps.android.interactors.mechanism.repository.MechanismRepository

class MechanismInteractor(
    private val repository: MechanismRepository,
) {

    suspend fun getMechanismTypes(): List<MechanismType> =
        repository.getMechanismTypes()

    suspend fun getMechanismListByType(typeId: Int) =
        repository.getMechanismListByType(typeId)

    suspend fun selectMechanism(mechanismId: Int) =
        repository.selectMechanism(mechanismId)

    suspend fun getCombinedMechanismList() =
        repository.getCombinedMechanismList()

    suspend fun startMechanismService() =
        repository.startMechanismService()

    suspend fun stopMechanismService() =
        repository.stopMechanismService()

    suspend fun sendTotalDistance(distance: Double) =
        repository.sendTotalDistance(distance)
}
