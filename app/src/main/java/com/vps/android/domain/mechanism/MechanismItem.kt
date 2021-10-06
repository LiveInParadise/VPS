package com.vps.android.domain.mechanism

import android.os.Parcelable
import com.vps.android.core.recycler.RecyclerViewItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class MechanismItem(
    val id: Int,
    val name: String,
    val workerId: Int?,
    val loadingGoods: Int,
    val inService: Int,
    val mechanismTypeId: Int,
    val mechanismTypeName: String,
) : Parcelable, RecyclerViewItem {

    override fun getId() = id

    fun inService() = inService == 1
}
