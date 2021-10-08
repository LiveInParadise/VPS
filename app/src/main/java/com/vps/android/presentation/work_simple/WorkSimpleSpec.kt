package com.vps.android.presentation.work_simple

import android.os.Parcelable
import com.vps.android.domain.task.TaskInfo
import kotlinx.parcelize.Parcelize

@Parcelize
data class WorkSimpleSpec(
    val taskInfo: TaskInfo,
) : Parcelable
