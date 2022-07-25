package ge.tarustashvili_tbabunashvili.finalproject.data.model

import android.graphics.drawable.Drawable
import com.google.firebase.database.IgnoreExtraProperties
import java.util.*


@IgnoreExtraProperties
data class Conversation(
    var convId: String? = null,
    val comb: String? = null,
    val message: String? = null,
    val date: Date? = null,
    val from: String? = null,
    val to: String? = null,
    val avatarFrom: String? = null,
    val avatarTo: String? = null,
    val nicknameFrom: String? = null,
    val nicknameTo: String? = null,
    val jobFrom: String? = null,
    val jobTo: String? = null
) : Comparable<Conversation> {
    override fun compareTo(other: Conversation): Int {
        return other.date!!.compareTo(date)
    }

}