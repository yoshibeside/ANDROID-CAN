package retrofit.api

import retrofit.model.ListCabangRestoran
import retrofit.model.ListMenu
import retrofit2.Call
import retrofit2.http.GET

interface APICall {
    @GET("v1/menu")
    fun getMenu(): Call<ListMenu>
    @GET("v1/branch")
    fun getRestoran(): Call<ListCabangRestoran>
    @GET("v1/menu/food")
    fun getFood(): Call<ListMenu>
    @GET("v1/menu/drink")
    fun getDrink(): Call<ListMenu>
}