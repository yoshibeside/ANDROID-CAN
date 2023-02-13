package retrofit.model

import com.google.gson.annotations.SerializedName

class Menu {
    @SerializedName("name")
     val name: String? = null

    @SerializedName("description")
     val description: String? = null

    @SerializedName("currency")
     val currency: String? = null

    @SerializedName("price")
     val price = 0

    @SerializedName("sold")
     val sold = 0

    @SerializedName("type")
     val type: String? = null //Getter
}