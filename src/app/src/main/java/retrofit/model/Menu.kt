package retrofit.model

import com.google.gson.annotations.SerializedName

class MenuItemStuff {
    @SerializedName("name")
     var name: String? = null

    @SerializedName("description")
    var description: String? = null

    @SerializedName("currency")
    var currency: String? = null

    @SerializedName("price")
    var price = 0

    @SerializedName("sold")
    var sold = 0

    @SerializedName("type")
    var type: String? = null //Getter
}