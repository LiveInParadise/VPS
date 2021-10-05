package com.vps.android.interactors.mechanism.api

import com.vps.android.core.network.base.BaseResponseObj
import com.vps.android.interactors.mechanism.response.MechanismObj
import com.vps.android.interactors.mechanism.response.MechanismTypeObj
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MechanismApi {

    /**
     * Get mechanism types
     **/
    @GET("/api/mechanism/types")
    suspend fun getMechanismTypes(): List<MechanismTypeObj>

    /**
     * Get mechanism list by id
     **/
    @GET("/api/mechanism/list")
    suspend fun getMechanismListByType(
        @Query("mechanism_type_id") typeId: Int
    ): List<MechanismObj>

    /**
     * Get combined mechanism list
     **/
    @GET("/api/mechanism/combined/list")
    suspend fun getCombinedMechanismList(): List<MechanismObj>

    /**
     * Mechanism service start
     **/
    @POST("/api/mechanism/service_start")
    suspend fun startMechanismService(): BaseResponseObj<String>

    /**
     * Mechanism service stop
     **/
    @POST("/api/mechanism/service_stop")
    suspend fun stopMechanismService(): BaseResponseObj<String>
}
