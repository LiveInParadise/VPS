package com.vps.android.presentation.task

import android.os.Parcelable
import com.vps.android.domain.mechanism.MechanismTypeClass
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddTaskSpec(
    val mechanismTypeClass: MechanismTypeClass,
) : Parcelable
