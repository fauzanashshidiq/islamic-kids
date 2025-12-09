    package com.pam.uas.data.remote.api

    import com.pam.uas.data.remote.response.ApiDoaResponse
    import retrofit2.Response
    import retrofit2.http.GET
    import retrofit2.http.Path

    interface ApiService {

        @GET("api")
        suspend fun getAllDoa(): Response<List<ApiDoaResponse>>

        @GET("api/{id}")
        suspend fun getDoaById(@Path("id") id: String): Response<ApiDoaResponse>

        @GET("api/doa/v1/random")
        suspend fun getRandomDoa(): Response<ApiDoaResponse>
    }