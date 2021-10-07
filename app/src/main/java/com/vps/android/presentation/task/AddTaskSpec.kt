package com.vps.android.presentation.task

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddTaskSpec(
    val mechanismTypeId: Int? = null,
) : Parcelable
