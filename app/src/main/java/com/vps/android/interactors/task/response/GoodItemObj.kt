package com.vps.android.interactors.task.response

import com.vps.android.core.network.base.Transformable
import com.vps.android.domain.task.FullGoodItem
import com.vps.android.domain.task.GoodItem

data class FullGoodItemObj(
    val category_name: String?,
    val goods_list: List<GoodItemObj>,
) : Transformable<FullGoodItem> {

    override fun transform(): FullGoodItem =
        FullGoodItem(
            categoryName = category_name,
            goodsList = goods_list.map { it.transform() },
        )
}

data class GoodItemObj(
    val id: Int,
    val name: String,
) : Transformable<GoodItem> {

    override fun transform(): GoodItem =
        GoodItem(
            id = id,
            name = name,
        )
}
