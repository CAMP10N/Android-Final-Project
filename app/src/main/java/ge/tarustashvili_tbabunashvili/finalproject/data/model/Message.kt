package ge.tarustashvili_tbabunashvili.finalproject.data.model

import com.google.firebase.database.IgnoreExtraProperties
import java.util.*



@IgnoreExtraProperties
data class Message(
    var messageId: String? = null,
    val from: String? = null,
    val to: String? = null,
    val message: String? = null,
    val date: Date? = null,
    val comb: String? = null
) : Comparable<Message> {
    override fun compareTo(other: Message): Int {
        return date!!.compareTo(other.date)
    }

}