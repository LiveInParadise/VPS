package com.vps.android.interactors.task.response

import com.vps.android.core.network.base.Transformable
import com.vps.android.domain.task.PlaceItem

data class PlaceItemObj(
    val id: Int,
    val name: String,
) : Transformable<PlaceItem> {

    override fun transform(): PlaceItem =
        PlaceItem(
            id = id,
            name = name,
        )
}
