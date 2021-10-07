package com.vps.android.domain.task

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FullGoodItem(
    val categoryName: String?,
    val goodsList: List<GoodItem>,
) : Parcelable, CreateTaskItem {

    override fun getId() = goodsList.map { it.id }.joinToString(",")
}

@Parcelize
data class GoodItem(
    val id: Int,
    val name: String,
) : Parcelable, CreateTaskItem {

    override fun getId() = id
}
