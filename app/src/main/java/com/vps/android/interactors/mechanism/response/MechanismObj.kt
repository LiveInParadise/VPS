package com.vps.android.interactors.mechanism.response

import com.vps.android.core.network.base.Transformable
import com.vps.android.domain.mechanism.MechanismItem

data class MechanismObj(
    val id: Int,
    val name: String,
    val mechanism_type_id: Int,
    val worker_id: Int?,
    val worker_name: String?,
    val loading_goods: Int,
    val in_service: Int?,
    val mechanism_type_name: String,
) : Transformable<MechanismItem> {

    override fun transform(): MechanismItem =
        MechanismItem(
            id = id,
            name = name,
            workerId = worker_id,
            workerName = worker_name,
            loadingGoods = loading_goods,
            inService = in_service ?: 0,
            mechanismTypeId = mechanism_type_id,
            mechanismTypeName = mechanism_type_name
        )
}
