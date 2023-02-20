package roomdb

import androidx.room.*

@Dao
interface KeranjangDao {
    @Query("SELECT * FROM menu_table")
    fun getAll(): List<MenuDatabase>

//    @Query("SELECT * FROM menu_table WHERE type_menu== :types")
//    fun findMenu(types: TypeMenu)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(menu: MenuDatabase)

    @Query("DELETE FROM menu_table")
    suspend fun deleteAll()
}