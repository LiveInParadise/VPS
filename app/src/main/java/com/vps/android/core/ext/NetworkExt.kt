package com.vps.android.core.ext

import okhttp3.Response

val Response.bodySnapshotUtf8: String?
    get() {
        val source = body?.source()
        source?.request(Long.MAX_VALUE)
        return source?.buffer?.snapshot()?.utf8()
    }
