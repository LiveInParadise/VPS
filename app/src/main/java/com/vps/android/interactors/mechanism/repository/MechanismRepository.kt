package com.vps.android.interactors.mechanism.repository

import com.vps.android.core.network.base.RequestResult
import com.vps.android.domain.mechanism.MechanismItem
import com.vps.android.domain.mechanism.MechanismType

interface MechanismRepository {

    suspend fun getMechanismTypes(): List<MechanismType>

    suspend fun getMechanismListByType(typeId: Int): List<MechanismItem>

    suspend fun getCombinedMechanismList(): List<MechanismItem>

    suspend fun startMechanismService(): RequestResult<String>

    suspend fun stopMechanismService(): RequestResult<String>
}
