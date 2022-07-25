package ge.tarustashvili_tbabunashvili.finalproject.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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