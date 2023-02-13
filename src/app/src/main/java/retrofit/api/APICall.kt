package retrofit.api

import retrofit.model.ListMenu
import retrofit2.Call
import retrofit2.http.GET

interface APICall {
    @GET("v1/menu")
    fun getMenu(): Call<ListMenu>
}