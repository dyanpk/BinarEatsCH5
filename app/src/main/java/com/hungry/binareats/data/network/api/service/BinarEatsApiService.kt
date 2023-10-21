package com.hungry.binareats.data.network.api.service

import com.hungry.binareats.BuildConfig
import com.hungry.binareats.data.network.api.model.category.CategoriesResponse
import com.hungry.binareats.data.network.api.model.menu.MenusResponse
import com.hungry.binareats.data.network.api.model.order.OrderRequest
import com.hungry.binareats.data.network.api.model.order.OrderResponse
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit


interface BinarEatsApiService{
    @GET("listmenu")
    suspend fun getMenus(@Query("category") category: String? = null): MenusResponse

    @GET("categories")
    suspend fun getCategories(): CategoriesResponse

    @POST("order")
    suspend fun createOrder(@Body orderRequest: OrderRequest): OrderResponse


    companion object {
        @JvmStatic
        operator fun invoke(): BinarEatsApiService {
            val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
            return retrofit.create(BinarEatsApiService::class.java)
        }
    }
}