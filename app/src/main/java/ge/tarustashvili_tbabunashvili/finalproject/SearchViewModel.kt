package ge.tarustashvili_tbabunashvili.finalproject

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import ge.tarustashvili_tbabunashvili.finalproject.data.model.User

class SearchViewModel(val rep: FriendsRepository): ViewModel() {
    private var repository = rep

    fun getFriendList():  MutableLiveData<MutableList<User>?> {
        return rep.getFriends()
    }

    fun getByNickname(nickname: String) {
        rep.getByNickname(nickname)
    }

}