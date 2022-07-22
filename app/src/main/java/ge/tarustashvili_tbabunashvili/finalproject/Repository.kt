package ge.tarustashvili_tbabunashvili.finalproject

import ge.tarustashvili_tbabunashvili.finalproject.data.UsersDB
import ge.tarustashvili_tbabunashvili.finalproject.data.entity.User

class Repository (private val db: UsersDB) {


    fun addUser(user: User): Long {
        return db.usersDao().addUser(user)
    }

    fun getUser(nickname: String, password: String): List<User> {
        return db.usersDao().getUser(nickname,password)
    }
}