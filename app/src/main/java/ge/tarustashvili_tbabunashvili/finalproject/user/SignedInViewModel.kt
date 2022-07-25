package ge.tarustashvili_tbabunashvili.finalproject.user

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import ge.tarustashvili_tbabunashvili.finalproject.data.model.Conversation
import ge.tarustashvili_tbabunashvili.finalproject.data.model.User
import ge.tarustashvili_tbabunashvili.finalproject.signin.Repository

class SignedInViewModel(val rep: Repository): ViewModel() {
    private var repository = rep
    private var userLiveData: MutableLiveData<FirebaseUser?> = repository.getUserLiveData()
    private var loggedOutLiveData : MutableLiveData<Boolean> = repository.getLoggedOutLiveData()

    fun logOut() {
        repository.logOut()
    }

    fun getLoggedOutLiveData(): MutableLiveData<Boolean> {
        return loggedOutLiveData
    }


    fun getCurrentUser(): MutableLiveData<User?> {
        return repository.getCurrentUser()
    }


    fun getByNickname(nickname: String, rec: String) {
        rep.getByNickname(nickname, rec)
    }

    fun registerConversationListener(from: String) {
        rep.registerConversationlistener(from)
    }

    fun getConversationsList(): MutableLiveData<MutableList<Conversation>?> {
        return rep.getConversationsList()
    }

    fun updateInfo(user: User) {
        repository.updateInfo(user)
    }

    fun uploadImage(user: User, uri: Uri) {
        repository.uploadImage(user, uri)
    }
}