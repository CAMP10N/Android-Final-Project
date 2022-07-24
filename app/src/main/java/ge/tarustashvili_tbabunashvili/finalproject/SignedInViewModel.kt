package ge.tarustashvili_tbabunashvili.finalproject

import android.app.Application
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import ge.tarustashvili_tbabunashvili.finalproject.data.model.User

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


    fun getCurrentUser(): MutableLiveData<User?> {
        return repository.getCurrentUser()
    }

    fun updateInfo(user: User) {
        repository.updateInfo(user)
    }

    fun uploadImage(user: User, uri: Uri) {
        repository.uploadImage(user, uri)
    }
}