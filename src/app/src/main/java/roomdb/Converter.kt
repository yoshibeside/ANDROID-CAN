package roomdb

import androidx.room.TypeConverter


    class Converter {
        @TypeConverter
        fun toType(value: String): TypeMenu = enumValueOf(value)

        @TypeConverter
        fun fromType(value: TypeMenu) : String = value.name
    }