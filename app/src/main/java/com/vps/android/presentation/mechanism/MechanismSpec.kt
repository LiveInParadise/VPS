package com.vps.android.presentation.mechanism

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MechanismSpec(
    val mechanismTypeId: Int
) : Parcelable
