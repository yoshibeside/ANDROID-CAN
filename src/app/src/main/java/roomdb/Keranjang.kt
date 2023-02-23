package roomdb

import androidx.room.*

@Entity(tableName = "menu_table", indices = [Index(value = ["name_menu", "description_menu"], unique = true)])
data class MenuDatabase(
    @PrimaryKey(autoGenerate = true) val id_cart_menu: Int?,
    @ColumnInfo(name = "name_menu") val name_menu: String?,
    @ColumnInfo (name = "type_menu") val type_menu: TypeMenu?,
    @ColumnInfo(name = "description_menu") val description: String?,
    @ColumnInfo(name = "price_menu") val price: Int?,
    @ColumnInfo(name = "sold_menu") val sold: Int?,
    @ColumnInfo(name = "currency_menu") val currencies: String?,
    @ColumnInfo(name = "amount_menu") val amount: Int?,
)
