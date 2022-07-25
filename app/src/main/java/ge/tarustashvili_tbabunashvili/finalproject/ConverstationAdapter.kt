package ge.tarustashvili_tbabunashvili.finalproject

import android.content.Context
import android.text.format.DateUtils
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

class ConverstationAdapter(private var items: List<Conversation>, private var context: Context, private var currentUser: User): RecyclerView.Adapter<ConverstationHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConverstationHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.reusable_converstations, parent, false)
        return ConverstationHolder(view)
    }

    override fun onBindViewHolder(holder: ConverstationHolder, position: Int) {

        var avatarlink = items[position].avatarFrom
        if (items[position].from == currentUser.username) {
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
        var datetxt = items[position].date?.let { parseDate(it) }
        holder.date.text = datetxt
    }

    override fun getItemCount(): Int {
        return items.size
    }

    private fun parseDate(date: Date): String {
        if (DateUtils.isToday(date.time)) {
            return DateUtils.getRelativeTimeSpanString(Date().time - date.time).toString()
        } else {
            val months = mapOf(1 to "Jan",
                2 to "Feb", 3 to "Mar", 4 to "Apr", 5 to "May", 6 to "Jun", 7 to "Jul", 8 to "Aug",
                9 to "Sep", 10 to "Oct", 11 to "Nov", 12 to "Dec")
            var dateStr = date.toString()
            var dt = ""
            dt += months[dateStr.substring(5, 7).toInt()]?.uppercase() + " " + dateStr.substring(8)
            return dt
        }
    }
}

class ConverstationHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    var pfp: ImageView = itemView.findViewById(R.id.profile_picture)
    var name: TextView = itemView.findViewById(R.id.name_lastname)
    var message: TextView = itemView.findViewById(R.id.last_message)
    var date: TextView = itemView.findViewById(R.id.date)
}