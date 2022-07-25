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
import ge.tarustashvili_tbabunashvili.finalproject.data.model.Conversation
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
    private var convos:  MutableLiveData<MutableList<Conversation>?> = MutableLiveData()
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
   //     Log.d("conversation", "ar amatebs")
       /* updateConversation(from,to)
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
        }*/
    }


    /*
    var convId: String? = null,
    val comb: String? = null,
    val message: String? = null,
    val date: Date? = null,
    val from: String? = null,
    val to: String? = null,
    val avatarFrom: String? = null,
    val avatarTo: String? = null,
    val nicknameFrom: String? = null,
    val nicknameTo: String? = null
     */

    fun updateConversation(from: String, to: String, time: Date, message: String, avatarFrom: String, avatarTo: String,
                                nicknameFrom: String, nicknameTo: String) {
        //removeConvo(getCombined(from,to))
        conversations
            .orderByChild("comb")
            .equalTo(getCombined(from,to))
            .get()
            .addOnSuccessListener {
                //   Log.d("ma",it.children.toString())
                var exists = false
                val newconv = Conversation(
                    comb = getCombined(from,to),
                    from = from,
                    to = to,
                    date = time,
                    message = message,
                    avatarFrom = avatarFrom,
                    avatarTo = avatarTo,
                    nicknameFrom = nicknameFrom,
                    nicknameTo = nicknameTo
                )
                for (obj in it.children) {
                    val singleConversation: Conversation = obj.getValue(Conversation::class.java) as Conversation
                    if (singleConversation.from == to || singleConversation.to == to) {
                        exists = true
                        obj.key?.let { it1 -> conversations.child(it1).setValue(newconv) }
                    }
                }
                if (!exists) {
                    conversations.push().key?.let { it1 ->
                        conversations.child(it1).setValue(
                            Conversation(
                                comb = getCombined(from,to),
                                from = from,
                                to = to,
                                date = time,
                                message = message,
                                avatarFrom = avatarFrom,
                                avatarTo = avatarTo,
                                nicknameFrom = nicknameFrom,
                                nicknameTo = nicknameTo
                            )
                        )
                    }
                }
            }
            .addOnFailureListener {
                //  Log.d("vasdasdsd", it.toString())
            }
        //writeNewConvo(from,to,time,message,avatarFrom,avatarTo, nicknameFrom, nicknameTo)
/*
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
            }*/
    }

    private fun removeConvo(comb: String) {
        conversations
            .orderByChild("comb")
            .equalTo(comb)
            .get()
            .addOnSuccessListener {
                for (obj in it.children) {
                    obj.key?.let { it1 -> conversations.child(it1).removeValue()}
                }
            }
            .addOnFailureListener {
              //  Log.d("vasdasdsd", it.toString())
            }
    }

    private fun writeNewConvo(from: String, to: String, time: Date, message: String, avatarFrom: String, avatarTo: String,
                              nicknameFrom: String, nicknameTo: String) {
        conversations.push().key?.let {
            conversations.child(it).setValue(
                Conversation(
                    comb = getCombined(from,to),
                    from = from,
                    to = to,
                    date = time,
                    message = message,
                    avatarFrom = avatarFrom,
                    avatarTo = avatarTo,
                    nicknameFrom = nicknameFrom,
                    nicknameTo = nicknameTo
                )
            )
        }
    }
    fun registerMessagesListener(from: String, to: String) {
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

    fun registerConversationlistener(from: String) {
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
        val conv = mutableListOf<Conversation>()
        conversations
            .orderByChild("from")
            .equalTo(from)
            .get()
            .addOnSuccessListener {
                for (obj in it.children) {
                    val singleConversation: Conversation = obj.getValue(Conversation::class.java) as Conversation
                    Log.d("esec", singleConversation.toString())
                    conv.add(singleConversation)
                }

                /*conversations
                    .orderByChild("to")
                    .equalTo(from)
                    .get()
                    .addOnSuccessListener { it1 ->
                        Log.d("ma",it1.children.toString())
                        for (obj in it1.children) {
                            val singleConversation: Conversation = obj.getValue(Conversation::class.java) as Conversation
                            conv.add(singleConversation)
                        }
                    }
                    .addOnFailureListener { it2 ->
                        Log.d("vasdasdsd", it2.toString())
                    }
                conv.sort()
                convos.postValue(conv)*/
            }
            .addOnFailureListener {
            }
        conversations
            .orderByChild("to")
            .equalTo(from)
            .get()
            .addOnSuccessListener { it1 ->
                for (obj in it1.children) {
                    val singleConversation: Conversation = obj.getValue(Conversation::class.java) as Conversation
                    conv.add(singleConversation)
                }
                conv.sort()
                convos.postValue(conv)
            }
            .addOnFailureListener { it2 ->
            }

       /* conversations
            .orderByChild("to")
            .equalTo(from)
            .get()
            .addOnSuccessListener {
                Log.d("ma",it.children.toString())
                for (obj in it.children) {
                    val singleConversation: Conversation = obj.getValue(Conversation::class.java) as Conversation
                    conv.add(singleConversation)
                }
            }
            .addOnFailureListener {
                Log.d("vasdasdsd", it.toString())
            }*/
        /*conv.sort()
        Log.d("ANSUMANE",conv.toString())
        Log.d("ANSUMANE12", from)
        convos.postValue(conv)*/
    }

    fun getConvos(): MutableLiveData<MutableList<Message>?> {
        return myConvos
    }

    fun getConversationsList(): MutableLiveData<MutableList<Conversation>?> {
        return convos
    }

    fun refreshData(from: String, to: String) {
        chats
            .orderByChild("comb")
            .equalTo(getCombined(from,to))
            .get()
            .addOnSuccessListener {
            //    Log.d("ma",it.children.toString())
                var messageList = mutableListOf<Message>()
                for (obj in it.children) {
                    var message: Message = obj.getValue(Message::class.java) as Message
                    messageList.add(message)
                }
                messageList.sort()
                myConvos.postValue(messageList)
            }
            .addOnFailureListener {
            //    Log.d("vasdasdsd", it.toString())
            }
    }



}