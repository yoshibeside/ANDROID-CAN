package retrofit.api

import okhttp3.Response
import okhttp3.ResponseBody
import retrofit.model.ListCabangRestoran
import retrofit.model.ListMenu
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface APICall {
    @GET("v1/menu")
    fun getMenu(): Call<ListMenu>
    @GET("v1/branch")
    fun getRestoran(): Call<ListCabangRestoran>
    @GET("v1/menu/food")
    fun getFood(): Call<ListMenu>
    @GET("v1/menu/drink")
    fun getDrink(): Call<ListMenu>
    @POST("v1/payment/{transaction_id}")
    fun postPayment(@Path("transaction_id") id: String): Call<ResponseBody>

}