package com.vps.android.domain.mechanism

import android.os.Parcelable
import com.vps.android.core.recycler.RecyclerViewItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class MechanismType(
    val id: Int,
    val name: String,
    val property: Int,
    val propertyName: String,
) : Parcelable, RecyclerViewItem {

    override fun getId() = id

    fun getType(): MechanismTypeClass = MechanismTypeClass.values().firstOrNull { it.property == property } ?: MechanismTypeClass.SIMPLE
}

enum class MechanismTypeClass(val property: Int) {
    SIMPLE(0),
    COMBINED(1)
}
