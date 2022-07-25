package ge.tarustashvili_tbabunashvili.finalproject

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ge.tarustashvili_tbabunashvili.finalproject.data.model.User
import java.util.*
import kotlin.collections.ArrayList

class FriendsRepository(context: Context) {
    private val users = Firebase.database.getReference("users")
    private var friends: MutableLiveData<MutableList<User>?> = MutableLiveData()
    init {
        users.get().addOnSuccessListener {
            //Log.d("wamoigoo?", it.children.count().toString())
            onFriendsFetched(it)
        }
    }

    fun getByNickname(nickname: String) {
        users
            .orderByChild("nickname")
            .equalTo(nickname)
            .get()
            .addOnSuccessListener {
                onFriendsFetched(it)
            }
    }

    private fun onFriendsFetched(dataSnapshot: DataSnapshot) {
        var friendList = mutableListOf<User>()
        for (obj in dataSnapshot.children) {
            var user: User = obj.getValue(User::class.java) as User
            friendList.add(user)
        }
        friends.postValue(friendList)
    }

    fun getFriends(): MutableLiveData<MutableList<User>?> {
        return friends
    }

}