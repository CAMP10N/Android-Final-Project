package ge.tarustashvili_tbabunashvili.finalproject

import android.content.Context
import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ge.tarustashvili_tbabunashvili.finalproject.data.model.Conversation
import ge.tarustashvili_tbabunashvili.finalproject.data.model.User
import java.util.*

class ConversationAdapter(var items: List<Conversation>, private var context: Context, var currentUserName: String, var listener: ConvoListener): RecyclerView.Adapter<ConverstationHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConverstationHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.reusable_converstations, parent, false)
        return ConverstationHolder(view)
    }

    override fun onBindViewHolder(holder: ConverstationHolder, position: Int) {
        var avatarlink = items[position].avatarFrom
        if (items[position].from == currentUserName) {
            //holder.pfp.setImageDrawable()
            avatarlink = items[position].avatarTo
            holder.name.text = items[position].nicknameTo
        }   else {
            holder.name.text = items[position].nicknameFrom
        }
        if (avatarlink != null && avatarlink != "") {
            Glide.with(context)
                .load(avatarlink)
                .circleCrop()
                .into(holder.pfp)
        }
        holder.message.text = items[position].message
        val datetxt = items[position].date?.let { parseDate(it) }
        Log.d("bbbbbbbbbbb", datetxt!!)
        holder.date.text = datetxt
        holder.itemView.setOnClickListener{
            listener.onClickListener(items[position])
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    private fun parseDate(date: Date): String {
        Log.d("datedate", date.toString())
        return if (DateUtils.isToday(date.time)) {
            val msS = Calendar.getInstance().getTimeInMillis()
            val msF = date.time
            val diff = msS - msF
            val diffSeconds = diff / 1000
            val diffMinutes = diff / (60 * 1000)
            val diffHours = diff / (60 * 60 * 1000)
            var relativeTime: String = ""

            if (diffMinutes < 1 && diffHours < 1) {
                relativeTime = diffSeconds.toString() + " sec"
            } else if (diffHours < 1) {
                relativeTime = diffMinutes.toString() + " min"
            } else {
                relativeTime = diffHours.toString() + " hour"
                if (diffHours > 1) relativeTime += "s"
            }
            relativeTime
        } else {
            val dateStr = date.toString()
            Log.d("strdat", dateStr)
            var dt = ""
            dt += dateStr.substring(8, dateStr.indexOf(' ', 8)) + " " + dateStr.substring(4, 7).uppercase()
            dt
        }
    }
}

class ConverstationHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    var pfp: ImageView = itemView.findViewById(R.id.profile_picture)
    var name: TextView = itemView.findViewById(R.id.name_lastname)
    var message: TextView = itemView.findViewById(R.id.last_message)
    var date: TextView = itemView.findViewById(R.id.date)
}

interface ConvoListener{
    fun onClickListener(conversation: Conversation)
}