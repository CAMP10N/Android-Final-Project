package ge.tarustashvili_tbabunashvili.finalproject.data

import androidx.room.Database
import androidx.room.RoomDatabase
import ge.tarustashvili_tbabunashvili.finalproject.data.dao.UserDAO
import ge.tarustashvili_tbabunashvili.finalproject.data.entity.User

@Database(entities = arrayOf(User::class), version = 1)
abstract class UsersDB(): RoomDatabase() {
    abstract fun usersDao(): UserDAO
}