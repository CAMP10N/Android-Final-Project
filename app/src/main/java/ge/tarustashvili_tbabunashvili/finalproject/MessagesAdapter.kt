package ge.tarustashvili_tbabunashvili.finalproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ge.tarustashvili_tbabunashvili.finalproject.data.model.Message
import java.util.*

class MessagesAdapter(private var curruser: String, var items: List<Message>): RecyclerView.Adapter<MessageHolder>() {

    fun ViewHolder0(parent: ViewGroup): MessageHolder {
        return MessageHolder(LayoutInflater.from(parent.context).inflate(R.layout.my_message, parent, false))
    }

    fun ViewHolder1(parent: ViewGroup): MessageHolder {
        return MessageHolder(LayoutInflater.from(parent.context).inflate(R.layout.his_message, parent, false))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageHolder {
        if (viewType == 0) {
            return ViewHolder0(parent)
        }
        return ViewHolder1(parent)
    }

    override fun onBindViewHolder(holder: MessageHolder, position: Int) {
        holder.message.text = items[position].message
        var datetxt = items[position].date?.let { parseDate(it) }
        holder.date.text = datetxt
    }

    override fun getItemCount(): Int {
        return items.size
    }

    private fun parseDate(date: Date): String {
        var d2s: String
        var hours = if (date.hours >= 10) date.hours.toString() else "0" + date.hours.toString()
        var minutes = if (date.minutes >= 10) date.minutes.toString() else "0" + date.minutes.toString()
        d2s = hours + ":" + minutes
        return d2s
    }

    override fun getItemViewType(position: Int): Int {
        if (items[position].from == curruser) return 0 else return 1
    }
}

class MessageHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    var message: TextView = itemView.findViewById(R.id.chat_chat)
    var date: TextView = itemView.findViewById(R.id.time)
}