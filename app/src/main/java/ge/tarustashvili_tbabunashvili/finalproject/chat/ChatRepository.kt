package ge.tarustashvili_tbabunashvili.finalproject.chat

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ge.tarustashvili_tbabunashvili.finalproject.data.model.Conversation
import ge.tarustashvili_tbabunashvili.finalproject.data.model.Message
import java.util.*


class ChatRepository(context: Context/*application: Application*/) {
    private var c = context
    private val chats = Firebase.database.getReference("chats")
    private val conversations = Firebase.database.getReference("conversations")
    private var myConvos: MutableLiveData<MutableList<Message>?> = MutableLiveData()


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
            ).addOnFailureListener {
                Toast.makeText(c, "Error while sending message!", Toast.LENGTH_SHORT).show()
                Log.e("Exception from server", it.toString())
            }
        }
    }
    fun updateConversation(from: String, to: String, time: Date, message: String, avatarFrom: String, avatarTo: String,
                                nicknameFrom: String, nicknameTo: String, jobFrom: String, jobTo: String) {
        conversations
            .orderByChild("comb")
            .equalTo(getCombined(from,to))
            .get()
            .addOnSuccessListener {
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
                    nicknameTo = nicknameTo,
                    jobFrom = jobFrom,
                    jobTo = jobTo
                )
                for (obj in it.children) {
                    val singleConversation: Conversation = obj.getValue(Conversation::class.java) as Conversation
                        exists = true
                        obj.key?.let { it1 -> conversations.child(it1).setValue(newconv) }
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
                                nicknameTo = nicknameTo,
                                jobFrom = jobFrom,
                                jobTo = jobTo
                            )
                        )
                    }
                }
            }
            .addOnFailureListener {
                Toast.makeText(c, "Error while retrieving data!", Toast.LENGTH_SHORT).show()
                Log.e("Exception from server", it.toString())
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

    fun getConvos(): MutableLiveData<MutableList<Message>?> {
        return myConvos
    }

    fun refreshData(from: String, to: String) {
        chats
            .orderByChild("comb")
            .equalTo(getCombined(from,to))
            .get()
            .addOnSuccessListener {
                var messageList = mutableListOf<Message>()
                for (obj in it.children) {
                    var message: Message = obj.getValue(Message::class.java) as Message
                    messageList.add(message)
                }
                messageList.sort()
                myConvos.postValue(messageList)
            }
            .addOnFailureListener {
                Toast.makeText(c, "Error while retrieving data!", Toast.LENGTH_SHORT).show()
                Log.e("Exception from server", it.toString())
            }
    }
}