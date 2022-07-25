package ge.tarustashvili_tbabunashvili.finalproject.signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser

class SignUpViewModel(val rep: SignUpRepository): ViewModel() {
    private var repository = rep

    fun register(nickname: String, password: String, job: String) {
        repository.register(nickname, password, job)
    }

    fun getUserLiveData(): MutableLiveData<FirebaseUser?> {
        return repository.getUserLiveData()
    }
}