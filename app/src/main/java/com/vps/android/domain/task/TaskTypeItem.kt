package com.vps.android.domain.task

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TaskTypeItem(
    val id: Int,
    val name: String,
    val mechanismTypeId: Int,
) : Parcelable, CreateTaskItem {

    override fun getId() = id
}
