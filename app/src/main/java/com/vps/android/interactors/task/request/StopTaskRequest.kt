package com.vps.android.interactors.task.request

data class StopTaskRequest(
    val end_time: String, // "2021-10-06 16:18:57"
    val distance: Double? = null, // Distance in km
)
