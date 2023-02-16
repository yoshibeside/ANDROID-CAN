package retrofit.model

import com.google.gson.annotations.SerializedName

class CabangRestoranModel {

    @SerializedName("name")
    val name : String? = null
    
    @SerializedName("popular_food")
    val popular_food : String? = null

    @SerializedName("address")
    val address : String? = null

    @SerializedName("contact_person")
    val contact_person : String? = null

    @SerializedName("phone_number")
    val phone_number : String? = null

    @SerializedName("longitude")
    val longitude : Number? = null

    @SerializedName("latitude")
    val latitude : Number? = null
}
