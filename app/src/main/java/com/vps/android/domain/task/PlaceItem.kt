package com.vps.android.domain.task

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlaceItem(
    val id: Int,
    val name: String,
) : Parcelable, CreateTaskItem {

    override fun getId() = id
}
