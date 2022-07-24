package ge.tarustashvili_tbabunashvili.finalproject

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser

class SignedInViewModel(val rep: Repository): ViewModel() {
    private var repository = rep
    private var userLiveData: MutableLiveData<FirebaseUser?> = repository.getUserLiveData()
    private var loggedOutLiveData : MutableLiveData<Boolean> = repository.getLoggedOutLiveData()

    fun logOut() {
        repository.logOut()
    }

    fun getUserLiveData(): MutableLiveData<FirebaseUser?> {
        return userLiveData
    }

    fun getLoggedOutLiveData(): MutableLiveData<Boolean> {
        return loggedOutLiveData
    }
}