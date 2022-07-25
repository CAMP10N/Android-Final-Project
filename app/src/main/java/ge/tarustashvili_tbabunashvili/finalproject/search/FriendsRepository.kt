package ge.tarustashvili_tbabunashvili.finalproject.search

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ge.tarustashvili_tbabunashvili.finalproject.data.model.User

class FriendsRepository(context: Context) {
    private val c = context
    private val users = Firebase.database.getReference("users")
    private var friends: MutableLiveData<MutableList<User>?> = MutableLiveData()
    init {
        users.get().addOnSuccessListener {
            onFriendsFetched(it)
        }
    }

    fun getByNickname(nickname: String) {
        users
            .orderByChild("nickname")
            .startAt(nickname)
            .endAt(nickname + "\uf8ff")
            .get()
            .addOnSuccessListener {
                onFriendsFetched(it)
            }
            .addOnFailureListener {
                Toast.makeText(c, "Error while retrieving data!", Toast.LENGTH_SHORT).show()
                Log.e("Exception from server", it.toString())
            }
    }

    private fun onFriendsFetched(dataSnapshot: DataSnapshot) {
        var friendList = mutableListOf<User>()
        for (obj in dataSnapshot.children) {
            var user: User = obj.getValue(User::class.java) as User
            Log.d("es", user.toString())
            friendList.add(user)
        }
        friends.postValue(friendList)
    }

    fun getFriends(): MutableLiveData<MutableList<User>?> {
        return friends
    }

}