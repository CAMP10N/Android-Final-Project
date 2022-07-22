package ge.tarustashvili_tbabunashvili.finalproject.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class User (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo
    var nickname: String,
    @ColumnInfo
    var password: String,
    @ColumnInfo
    var job: String,
) {

}