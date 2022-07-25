package ge.tarustashvili_tbabunashvili.finalproject

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ge.tarustashvili_tbabunashvili.finalproject.data.model.Conversation
import ge.tarustashvili_tbabunashvili.finalproject.data.model.Message
import java.util.*

class ChatViewModel(val rep: ChatRepository): ViewModel() {
    private var repository = rep

    fun sendMessage(from: String, to: String, message: String, time: Date) {
        rep.sendMessage(from, to,message,time)
    }

    fun registerListener(from: String, to: String) {
        rep.registerMessagesListener(from,to)
    }

    fun registerConversationListener(from: String) {
        rep.registerConversationlistener(from)
    }

    fun getConvos(): MutableLiveData<MutableList<Message>?> {
        return rep.getConvos()
    }

    fun getConversationsList(): MutableLiveData<MutableList<Conversation>?> {
        return rep.getConversationsList()
    }

    fun updateConversation(from: String, to: String, time: Date, message: String, avatarFrom: String, avatarTo: String,
                           nicknameFrom: String, nicknameTo: String, jobFrom: String, jobTo: String) {
        rep.updateConversation(from,to,time,message,avatarFrom,avatarTo, nicknameFrom, nicknameTo, jobFrom, jobTo)
    }
}