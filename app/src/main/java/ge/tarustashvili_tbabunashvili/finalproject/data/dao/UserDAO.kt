package ge.tarustashvili_tbabunashvili.finalproject.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ge.tarustashvili_tbabunashvili.finalproject.data.entity.User

@Dao
interface UserDAO {
    @Insert
    fun addUser(user: User):Long

    @Query("select * from User where nickname= :nick and password=:pass")
    fun getUser(nick: String, pass: String): List<User>
}