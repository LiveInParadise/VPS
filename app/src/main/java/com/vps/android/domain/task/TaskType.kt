package com.vps.android.domain.task

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TaskType(
    val id: Int,
    val name: String,
    val mechanismTypeId: Int,
) : Parcelable
