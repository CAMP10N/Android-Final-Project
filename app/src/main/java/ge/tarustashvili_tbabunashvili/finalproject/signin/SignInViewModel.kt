package ge.tarustashvili_tbabunashvili.finalproject.signin


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser


class SignInViewModel(val rep: Repository): ViewModel() {
    private var repository = rep

    fun login(nickname: String, password: String) {
        repository.login(nickname, password)
    }

    fun getUserLiveData(): MutableLiveData<FirebaseUser?> {
        return repository.getUserLiveData()
    }
}