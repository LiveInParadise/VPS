package com.vps.android.interactors.mechanism.response

import com.vps.android.core.network.base.Transformable
import com.vps.android.domain.mechanism.MechanismType

data class MechanismTypeObj(
    val id: Int,
    val name: String,
    val property: Int,
    val property_name: String,
) : Transformable<MechanismType> {

    override fun transform(): MechanismType =
        MechanismType(
            id = id,
            name = name,
            property = property,
            propertyName = property_name,
        )
}
