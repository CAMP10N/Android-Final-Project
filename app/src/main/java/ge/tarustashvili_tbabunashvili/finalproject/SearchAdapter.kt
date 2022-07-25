package ge.tarustashvili_tbabunashvili.finalproject

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ge.tarustashvili_tbabunashvili.finalproject.data.model.User

class SearchAdapter(var searchActivity: SearchActivity, var items: MutableList<User>, var listener: FriendListListener): RecyclerView.Adapter<SearchHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.looked_up_user, parent, false)
        return SearchHolder(view)
    }

    override fun onBindViewHolder(holder: SearchHolder, position: Int) {
        if (items[holder.adapterPosition].avatar != null && items[holder.adapterPosition].avatar != "") {
            Glide.with(searchActivity)
                .load(items[holder.adapterPosition].avatar)
                .circleCrop()
                .into(holder.pfp)
        }   else {
            holder.pfp.setImageResource(R.drawable.avatar_image_placeholder)
        }
        holder.name.text = items[position].nickname
     //   Log.d("friendlist", holder.name.text.toString())
        holder.job.text = items[position].job
        holder.itemView.setOnClickListener{
            listener.onClickListener(items[position])
        }
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