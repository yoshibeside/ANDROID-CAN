package roomdb

import androidx.room.*

@Dao
interface KeranjangDao {
    @Query("SELECT * FROM menu_table")
    fun getAll(): List<MenuDatabase>

    @Query("SELECT * FROM menu_table WHERE amount_menu > 0")
    fun findMenu(): List<MenuDatabase>

    @Query("SELECT * FROM menu_table WHERE type_menu == :type")
    fun findByType(type: TypeMenu): List<MenuDatabase>

    @Query("UPDATE menu_table SET amount_menu=:amount_item WHERE name_menu = :name")
    fun  update(name: String, amount_item: Int)

    @Query("UPDATE menu_table SET amount_menu=0")
    fun  update()

    @Query("SELECT SUM(amount_menu * price_menu) FROM menu_table")
    fun  getTotalPrice(): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(menu: MenuDatabase)

    @Query("DELETE FROM menu_table")
    suspend fun deleteAll()
}