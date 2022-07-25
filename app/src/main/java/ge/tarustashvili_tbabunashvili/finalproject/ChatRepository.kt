package ge.tarustashvili_tbabunashvili.finalproject

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ge.tarustashvili_tbabunashvili.finalproject.data.model.Message
import java.util.*


class ChatRepository(context: Context/*application: Application*/) {

    //  private lateinit var application: Application
    private var c = context
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    // private var userLiveData: MutableLiveData<FirebaseUser?> = MutableLiveData()
    // private var loggedOutLiveData: MutableLiveData<Boolean> = MutableLiveData()
    private val chats = Firebase.database.getReference("chats")
    private val users = Firebase.database.getReference("users")
    private val conversations = Firebase.database.getReference("conversations")

    private var myConvos: MutableLiveData<MutableList<Message>?> = MutableLiveData()

    init {
      /*  if (firebaseAuth.currentUser != null) {
            Log.d("aq", "shemovida chat ")
            users.child(firebaseAuth.currentUser!!.uid).get().addOnSuccessListener {
                Log.d("ratom", "gamirbixar 123")

                currentUser = User(
                    job = it.child("job").getValue<String>(),
                    nickname = it.child("nickname").getValue<String>(),
                    username = it.child("username").getValue<String>(),
                    avatar = it.child("avatar").getValue<String>()
                )

            }.addOnFailureListener {
                Log.d("ratom", "memalebi 234")
            }
        }*/
    }


    fun getCombined(from: String, to:String) : String {
        if (from < to) {
            return "${from}_${to}"
        }
        return "${to}_${from}"
    }

    fun sendMessage(from: String, to: String, message: String, time: Date) {
        chats.push().key?.let {
            chats.child(it).setValue(
                Message(
                    from = from,
                    to = to,
                    message = message,
                    date = time,
                    comb = getCombined(from,to)
                )
            )
        }
        Log.d("conversation", "ar amatebs")
        updateConversation(from,to)
        Log.d("conversation222", "ar amatebs222")
        conversations.push().key?.let {
            conversations.child(it).setValue(
                Message(
                    from = from,
                    to = to,
                    message = message,
                    date = time,
                    comb = getCombined(from, to)
                )
            )
        }
    }

    fun updateConversation(from: String, to: String) {
        conversations
            .orderByChild("comb")
            .equalTo(getCombined(from,to))
            .get()
            .addOnSuccessListener {
                Log.d("ma",it.children.toString())
                var messageList = mutableListOf<Message>()
                for (obj in it.children) {
                    obj.key?.let { it1 -> conversations.child(it1).removeValue()}
                }
            }
            .addOnFailureListener {
                Log.d("vasdasdsd", it.toString())
            }
    }


    fun registerMessagesListener(from: String, to: String) {
        val registerTime = Date()

        val listener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                refreshData(from, to)
            }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        }

        chats
            .orderByChild("comb")
            .equalTo(getCombined(from,to))
            .addChildEventListener(listener)

    }

    fun getConvos(): MutableLiveData<MutableList<Message>?> {
        return myConvos
    }

    fun refreshData(from: String, to: String) {
        chats
            .orderByChild("comb")
            .equalTo(getCombined(from,to))
            .get()
            .addOnSuccessListener {
                Log.d("ma",it.children.toString())
                var messageList = mutableListOf<Message>()
                for (obj in it.children) {
                    var message: Message = obj.getValue(Message::class.java) as Message
                    messageList.add(message)
                }
                messageList.sort()
                myConvos.postValue(messageList)
            }
            .addOnFailureListener {
                Log.d("vasdasdsd", it.toString())
            }
    }



}