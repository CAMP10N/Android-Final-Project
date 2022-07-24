package ge.tarustashvili_tbabunashvili.finalproject

import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ge.tarustashvili_tbabunashvili.finalproject.data.model.Converstation
import ge.tarustashvili_tbabunashvili.finalproject.data.model.User
import java.util.*

class SearchAdapter(var searchActivity: SearchActivity, var items: MutableList<User>, var listener: FriendListListener): RecyclerView.Adapter<SearchHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.looked_up_user, parent, false)
        return SearchHolder(view)
    }

    override fun onBindViewHolder(holder: SearchHolder, position: Int) {
        if (items[position].avatar != null && items[position].avatar != "") {
            Glide.with(searchActivity)
                .load(items[position].avatar)
                .circleCrop()
                .into(holder.pfp)
        }
        holder.name.text = items[position].nickname
        Log.d("friendlist", holder.name.text.toString())
        holder.job.text = items[position].job
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

class SearchHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    var pfp: ImageView = itemView.findViewById(R.id.searched_profile_picture)
    var name: TextView = itemView.findViewById(R.id.searched_name_lastname)
    var job: TextView = itemView.findViewById(R.id.searched_job)
}


interface FriendListListener{
    fun onClickListener(user: User)
}