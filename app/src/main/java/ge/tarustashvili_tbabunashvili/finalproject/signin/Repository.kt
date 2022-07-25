package ge.tarustashvili_tbabunashvili.finalproject.signin

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import ge.tarustashvili_tbabunashvili.finalproject.data.model.Conversation
import ge.tarustashvili_tbabunashvili.finalproject.data.model.User
import java.util.*


class Repository (context: Context/*application: Application*/) {
    private var c = context
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var userLiveData: MutableLiveData<FirebaseUser?> = MutableLiveData()
    private var loggedOutLiveData: MutableLiveData<Boolean> = MutableLiveData()
    private val users = Firebase.database.getReference("users")
    private val conversations = Firebase.database.getReference("conversations")
    private var currentUser: MutableLiveData<User?> = MutableLiveData()
    private var convos:  MutableLiveData<MutableList<Conversation>?> = MutableLiveData()
    init {
        if (firebaseAuth.currentUser != null) {
            userLiveData.postValue(firebaseAuth.currentUser)
            users.child(firebaseAuth.currentUser!!.uid).get().addOnSuccessListener {
                currentUser.postValue(User(job = it.child("job").getValue<String>(),
                    nickname = it.child("nickname").getValue<String>(),
                    username = it.child("username").getValue<String>(),
                    avatar = it.child("avatar").getValue<String>()
                ))
            }.addOnFailureListener {
                Toast.makeText(c, "Error while retrieving data!", Toast.LENGTH_SHORT).show()
                Log.e("Exception from server", it.toString())
            }
        }
        loggedOutLiveData.postValue(firebaseAuth.currentUser == null)
    }

    fun getUserLiveData(): MutableLiveData<FirebaseUser?> {
        return userLiveData
    }

    fun getLoggedOutLiveData(): MutableLiveData<Boolean> {
        return loggedOutLiveData
    }

    fun getCurrentUser(): MutableLiveData<User?> {
        return currentUser
    }

    fun registerConversationlistener(from: String) {
        refreshConversations(from)
        val listener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                refreshConversations(from)
            }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                refreshConversations(from)
            }
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        }

        conversations
            .orderByChild("from")
            .equalTo(from)
            .addChildEventListener(listener)
        conversations
            .orderByChild("to")
            .equalTo(from)
            .addChildEventListener(listener)
    }


    private fun refreshConversations(from: String) {
        conversations
            .get()
            .addOnSuccessListener {
                val conv = mutableListOf<Conversation>()
                for (obj in it.children) {
                    val singleConversation: Conversation = obj.getValue(Conversation::class.java) as Conversation
                    if (singleConversation.from == from || singleConversation.to == from) conv.add(singleConversation)
                }
                conv.sort()
                convos.postValue(conv)
            }
            .addOnFailureListener {
                Toast.makeText(c, "Error while retrieving data!", Toast.LENGTH_SHORT).show()
                Log.e("Exception from server", it.toString())
            }
    }

    fun getConversationsList(): MutableLiveData<MutableList<Conversation>?> {
        return convos
    }


    fun getByNickname(nickname: String, rec: String) {
        conversations
            .get()
            .addOnSuccessListener {
                val conv = mutableListOf<Conversation>()
                for (obj in it.children) {
                    val singleConversation: Conversation = obj.getValue(Conversation::class.java) as Conversation
                    if (singleConversation.from != rec && singleConversation.to != rec) continue
                    if ((singleConversation.from!!.startsWith(nickname)) || (singleConversation.to!!.startsWith(nickname)))
                        conv.add(singleConversation)
                }
                conv.sort()
                convos.postValue(conv)
            }
            .addOnFailureListener {
                Toast.makeText(c, "Error while retrieving data!", Toast.LENGTH_SHORT).show()
                Log.e("Exception from server", it.toString())
            }
    }

    fun login(nickname: String, password: String){
        firebaseAuth.signInWithEmailAndPassword(nickname,password)
            .addOnSuccessListener {
                userLiveData.postValue(firebaseAuth.currentUser)
            }
            .addOnFailureListener {
                Toast.makeText(c, "Log in failed", Toast.LENGTH_LONG).show()
                Log.e("Exception from server", it.toString())
            }
    }

    fun logOut() {
        firebaseAuth.signOut()
        loggedOutLiveData.postValue(true)
        currentUser.postValue(null)
    }

    fun updateInfo(user: User) {
        users.child(firebaseAuth.currentUser?.uid!!).setValue(user)
        currentUser.postValue(user)
        updateConversations(user)
    }

    private fun updateConversations(user: User) {
        conversations
            .get()
            .addOnSuccessListener {
                for (obj in it.children) {
                    val singleConversation: Conversation = obj.getValue(Conversation::class.java) as Conversation
                    if (singleConversation.from == user.username) {
                        obj.key?.let { it1 -> conversations.child(it1).setValue(
                            Conversation(
                                comb=singleConversation.comb,
                                message = singleConversation.message,
                                date = singleConversation.date,
                                from = singleConversation.from,
                                to = singleConversation.to,
                                avatarFrom = user.avatar,
                                jobFrom = user.job,
                                nicknameFrom = user.nickname,
                                avatarTo = singleConversation.avatarTo,
                                jobTo = singleConversation.jobTo,
                                nicknameTo = singleConversation.nicknameTo
                            )) }
                        continue
                    }
                    if (singleConversation.to == user.username) {
                        obj.key?.let { it1 -> conversations.child(it1).setValue(
                            Conversation(
                                comb=singleConversation.comb,
                                message = singleConversation.message,
                                date = singleConversation.date,
                                from = singleConversation.from,
                                to = singleConversation.to,
                                avatarTo = user.avatar,
                                jobTo = user.job,
                                nicknameTo = user.nickname,
                                avatarFrom = singleConversation.avatarFrom,
                                jobFrom = singleConversation.jobFrom,
                                nicknameFrom = singleConversation.nicknameFrom
                            )) }
                    }
                }
            }
            .addOnFailureListener {
                Toast.makeText(c, "Error while retrieving data!", Toast.LENGTH_SHORT).show()
                Log.e("Exception from server", it.toString())
            }
    }

    fun uploadImage(user: User, uri: Uri) {
        val storageRef = Firebase.storage.reference
        val imageRef = storageRef.child("images/" + UUID.randomUUID().toString());
        var uploadTask = imageRef.putFile(uri!!)
        val urlTask = uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            imageRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                updateInfo(User(username = user.username, nickname = user.nickname, job = user.job, avatar = downloadUri.toString()))
            }
        }.addOnFailureListener {
            Toast.makeText(c, "Error while uploading image!", Toast.LENGTH_SHORT).show()
            Log.e("Exception from server", it.toString())
        }
    }
}