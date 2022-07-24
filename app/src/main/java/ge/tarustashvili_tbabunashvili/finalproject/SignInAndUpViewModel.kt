package ge.tarustashvili_tbabunashvili.finalproject


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser


class SignInAndUpViewModel(val rep: Repository): ViewModel() {
    private var repository = rep
    private var userLiveData: MutableLiveData<FirebaseUser?> = repository.getUserLiveData()

    fun login(nickname: String, password: String) {
        repository.login(nickname, password)
    }

    fun register(nickname: String, password: String, job: String) {
        repository.register(nickname, password, job)
    }

    fun getUserLiveData(): MutableLiveData<FirebaseUser?> {
        return userLiveData
    }
}