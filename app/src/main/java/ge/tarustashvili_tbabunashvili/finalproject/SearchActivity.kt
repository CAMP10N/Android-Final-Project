package ge.tarustashvili_tbabunashvili.finalproject

import android.app.Application
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import ge.tarustashvili_tbabunashvili.finalproject.data.model.User
import java.util.*

class SearchActivity : AppCompatActivity(), FriendListListener {
    val searchViewModel: SearchViewModel by lazy {
        ViewModelProvider(this, SearchViewModelFactory(application)).get(SearchViewModel::class.java)
    }

    private val NO_DATA = "Not Found"
    private lateinit var currentUsername:String
    private lateinit var currentNickname:String
    private lateinit var currentJob: String
    private val lst = mutableListOf<User>()
    private var adapter = SearchAdapter(this,lst,this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        searchViewModel.getFriendList().observe(this, Observer {
            if (it != null) {
                update(it)
            }
        })
        currentUsername = intent.getStringExtra("current username")?: NO_DATA
        Log.d("user in intent", currentUsername)
        currentNickname = intent.getStringExtra("current nickname")?: NO_DATA
        currentJob = intent.getStringExtra("current job")?: NO_DATA

        findViewById<RecyclerView>(R.id.friendlist).adapter = adapter
        findViewById<EditText>(R.id.searchfriends).addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                val nick = findViewById<EditText>(R.id.searchfriends).text.toString()
                if (nick.length > 2) {
                    var runnable: Runnable? = null
                    var handler: Handler = Handler()
                    if (runnable != null)
                        handler.removeCallbacks(runnable!!)
                    runnable = Runnable {
                        searchViewModel.getByNickname(nick)
                    }
                    handler.postDelayed(runnable!!, 500);
                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {

            }
        })

    }

    private fun update(lst: MutableList<User>) {
        adapter.items = lst
        Log.d("aba aq", lst.toString())
        adapter.notifyDataSetChanged()
    }

    override fun onClickListener(user: User) {
        Log.d("adsadasdsdsd","mamsndad")
        var intent = Intent(this, ChatActivity::class.java).apply {
            putExtra(ChatActivity.nick, user.nickname)
            putExtra(ChatActivity.mail, user.username)
            putExtra(ChatActivity.job, user.job)
            putExtra(ChatActivity.pfp, user.avatar)
            putExtra(ChatActivity.from, currentUsername)
        }
        startActivity(intent)
    }

    fun onBack(view: View) {
        finish()
    }
}


class SearchViewModelFactory(var application: Application) : ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(FriendsRepository(this.application.applicationContext)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}