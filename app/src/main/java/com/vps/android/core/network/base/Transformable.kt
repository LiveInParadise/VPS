package com.vps.android.core.network.base

interface Transformable<T> {

    fun transform(): T
}

fun <T> List<Transformable<T>>.transform(): List<T> =
    map { it.transform() }
