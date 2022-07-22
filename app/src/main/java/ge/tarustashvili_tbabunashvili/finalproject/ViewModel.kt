package ge.tarustashvili_tbabunashvili.finalproject

import androidx.lifecycle.ViewModel
import ge.tarustashvili_tbabunashvili.finalproject.data.entity.User

class ViewModel(val rep: Repository): ViewModel() {

    fun saveNewUser(user: User): Long {
        return rep.addUser(user)
    }

    fun getUser(nickname: String, password: String): List<User> {
        return rep.getUser(nickname,password)
    }
}