package com.vps.android.core.utils

sealed class LocationCheckType {

    object StartFullDistance : LocationCheckType()
    object StopFullDistance : LocationCheckType()

    object StartTaskDistance : LocationCheckType()
    object StopTaskDistance : LocationCheckType()

}
