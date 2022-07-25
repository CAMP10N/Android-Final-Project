package ge.tarustashvili_tbabunashvili.finalproject

import android.app.Application
import android.app.appsearch.SearchResult
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
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import ge.tarustashvili_tbabunashvili.finalproject.data.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.Flow
import kotlin.coroutines.CoroutineContext

class SearchActivity : AppCompatActivity(), FriendListListener, CoroutineScope {
    val searchViewModel: SearchViewModel by lazy {
        ViewModelProvider(this, SearchViewModelFactory(application)).get(SearchViewModel::class.java)
    }

    private val NO_DATA = "Not Found"
    private lateinit var currentUsername:String
    private lateinit var currentNickname:String
    private lateinit var currentJob: String
    private lateinit var currentAvatar: String
    private lateinit var progressBar: ProgressBar
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
        currentUsername = intent.getStringExtra(myun)?: NO_DATA
      //  Log.d("user in intent", currentUsername)
        currentNickname = intent.getStringExtra(myn)?: NO_DATA
        currentJob = intent.getStringExtra(myj)?: NO_DATA
        currentAvatar = intent.getStringExtra(mya)?:""
        findViewById<RecyclerView>(R.id.friendlist).adapter = adapter
        progressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.VISIBLE
        findViewById<RecyclerView>(R.id.friendlist).visibility = View.INVISIBLE
        /*fun executeSearch(term: String): Flow<SearchResult> { ... }

        findViewById<EditText>(R.id.searchfriends).textChanges()
            .filterNot { findViewById<EditText>(R.id.searchfriends).isNullOrBlank() }
            .debounce(300)
            .distinctUntilChanged()
            .flatMapLatest { executeSearch(it) }
            .onEach { updateUI(it) }
            .launchIn(lifecycleScope)
*/

        val watcher = object :TextWatcher{
            private var searchFor = ""

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchText = s.toString().trim()
                if (searchText == searchFor)
                    return

                searchFor = searchText


                launch {
                    delay(300)  //debounce timeOut
                    if (searchText != searchFor)
                        return@launch
                    if (searchText.length > 2) {
                        progressBar.visibility = View.VISIBLE
                        findViewById<RecyclerView>(R.id.friendlist).visibility = View.INVISIBLE
                        searchViewModel.getByNickname(searchText)
                    }   else if (searchText == "") {
                        progressBar.visibility = View.VISIBLE
                        findViewById<RecyclerView>(R.id.friendlist).visibility = View.INVISIBLE
                        searchViewModel.getByNickname("")
                    }

                }
            }

            override fun afterTextChanged(s: Editable?) = Unit
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
        }
        findViewById<EditText>(R.id.searchfriends).addTextChangedListener(watcher)
/*
        val watcher = object : TextWatcher {
            private var name = ""

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val text = s.toString().trim()
                Log.d("eh", text)
                if (text.length > 2) {
                    var runnable: Runnable? = null
                    var handler: Handler = Handler()
                    if (runnable != null)
                        handler.removeCallbacks(runnable!!)
                    runnable = Runnable {
                        searchViewModel.getByNickname(text)
                    }
                    handler.postDelayed(runnable!!, 500);
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
        }*/
/*

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
*/

    }

    companion object {
        const val myun = "my username"
        const val myj = "my job"
        const val myn = "my nickname"
        const val mya = "my avatar"
    }

    private fun update(lst: MutableList<User>) {
        val newLst = mutableListOf<User>()
        for (user in lst) {
            if (user.username == currentUsername) continue
            newLst.add(user)
        }
        progressBar.visibility = View.GONE
        findViewById<RecyclerView>(R.id.friendlist).visibility = View.VISIBLE
        adapter.items = newLst
        adapter.notifyDataSetChanged()
        if (newLst.isEmpty()) {
            Toast.makeText(this, "User Not Found", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onClickListener(user: User) {
       // Log.d("adsadasdsdsd","mamsndad")
        Log.d("ra xdeba ver gevige", user.avatar.toString())
        var intent = Intent(this, ChatActivity::class.java).apply {
            putExtra(ChatActivity.tonick, user.nickname)
            putExtra(ChatActivity.tomail, user.username)
            putExtra(ChatActivity.tojob, user.job)
            putExtra(ChatActivity.topfp, user.avatar)
            putExtra(ChatActivity.frommail, currentUsername)
            putExtra(ChatActivity.fromnick, currentNickname)
            putExtra(ChatActivity.frompfp, currentAvatar)
            putExtra(ChatActivity.fromjob, currentJob)
        }
        startActivity(intent)
    }

    fun onBack(view: View) {
        finish()
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main
}


class SearchViewModelFactory(var application: Application) : ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(FriendsRepository(this.application.applicationContext)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}