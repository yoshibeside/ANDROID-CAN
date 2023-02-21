package retrofit.model

import androidx.lifecycle.ViewModel
import com.google.gson.annotations.SerializedName

class MenuItemStuff: ViewModel() {
    @SerializedName("name")
    var name: String? = null

    @SerializedName("description")
    var description: String? = null

    @SerializedName("currency")
    var currency: String? = null

    @SerializedName("price")
    var price= 0
    @SerializedName("sold")
    var sold = 0

    @SerializedName("type")
    var type: String? = null //Getter

    private var amount= 0

    fun incrementAmount() {
        if (amount!= null) {
            amount = amount+ 1
        }
    }

    fun decrementAmount() {
        if (amount!= null && amount != 0) {
            amount = amount - 1
        }
    }

    fun getAmount(): Int {
        return amount
    }

}


