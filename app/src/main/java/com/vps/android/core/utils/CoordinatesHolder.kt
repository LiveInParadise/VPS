package com.vps.android.core.utils

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vps.android.presentation.base.Event

class CoordinatesHolder {

    private val _fullDistanceTrackingLive = MutableLiveData<Event<LocationCheckType>>()
    val fullDistanceTrackingLive: LiveData<Event<LocationCheckType>> = _fullDistanceTrackingLive

    private val _taskDistanceTrackingLive = MutableLiveData<Event<LocationCheckType>>()
    val taskDistanceTrackingLive: LiveData<Event<LocationCheckType>> = _taskDistanceTrackingLive

    private var lastFullDistanceCoordinate: Location? = null
    private var lastTaskDistanceCoordinate: Location? = null

    private var fullDistance: Double = 0.0
        get() = field
    private var taskDistance: Double = 0.0
        get() = field

    fun addFullDistanceCoordinate(newLocation: Location) {
        lastFullDistanceCoordinate?.let { lastLocation ->
            fullDistance += lastLocation.distanceTo(newLocation)
        }

        lastFullDistanceCoordinate = newLocation
    }

    fun addTaskDistanceCoordinate(newLocation: Location) {
        lastTaskDistanceCoordinate?.let { lastLocation ->
            taskDistance += lastLocation.distanceTo(newLocation)
        }

        lastTaskDistanceCoordinate = newLocation
    }

    fun sendFullDistanceEvent(command: LocationCheckType) {
        _fullDistanceTrackingLive.value = Event(command)
    }

    fun sendTaskDistanceEvent(command: LocationCheckType) {
        _taskDistanceTrackingLive.value = Event(command)
    }

    fun getFullMechanismDistance() = fullDistance / 1000
    fun getTaskMechanismDistance() = taskDistance / 1000

    fun clearFullDistanceTrackData() {
        fullDistance = 0.0
    }

    fun clearTaskDistanceTrackData() {
        taskDistance = 0.0
    }
}
