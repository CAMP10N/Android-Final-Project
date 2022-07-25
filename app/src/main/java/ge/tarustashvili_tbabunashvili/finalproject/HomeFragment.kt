package ge.tarustashvili_tbabunashvili.finalproject

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import ge.tarustashvili_tbabunashvili.finalproject.data.model.Conversation
import ge.tarustashvili_tbabunashvili.finalproject.data.model.Message
import ge.tarustashvili_tbabunashvili.finalproject.data.model.User
import ge.tarustashvili_tbabunashvili.finalproject.databinding.HomePageFragmentBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class HomeFragment: Fragment(), ConvoListener, CoroutineScope {


    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private var _binding: HomePageFragmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var rvConv: RecyclerView
    private lateinit var user: User
    val chatViewModel: ChatViewModel by lazy {
        ViewModelProvider(this, ChatViewModelsFactory(requireActivity().application)).get(ChatViewModel::class.java)
    }

    val signedInViewModel: SignedInViewModel by lazy {
        ViewModelProvider(requireActivity(), SignedInViewModelsFactory(requireActivity().application)).get(SignedInViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       // Log.d("home", "oncreate")
        _binding = HomePageFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvConv = view.findViewById(R.id.conversations)
        rvConv.adapter = ConversationAdapter(emptyList(), requireContext(),"", this)
        signedInViewModel.getCurrentUser().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                chatViewModel.registerConversationListener(it.username?:"")
                user = it
                (rvConv.adapter as ConversationAdapter).currentUserName = it.username?: ""
               // Log.d("ha???", "sadas")
            }   else {
               // Log.d("eeh","eeh")
            }
        })

        chatViewModel.getConversationsList().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                updateData(it)
               // Log.d("ras shobi", it.toString())
            }
        })


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
                    chatViewModel.getByNickname(searchText)

                }
            }

            override fun afterTextChanged(s: Editable?) = Unit
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
        }
        view.findViewById<EditText>(R.id.message_edit_text).addTextChangedListener(watcher)
    }

    fun updateData(conversations: MutableList<Conversation>)   {
       // Log.d("shemovida?", " upalo")
       // Log.d("aba raga ginda", conversations.toString())
        (rvConv.adapter as ConversationAdapter).items = conversations
        (rvConv.adapter as ConversationAdapter).notifyDataSetChanged()
        if (conversations.isEmpty()) {
            Toast.makeText(requireContext(), "User Not Found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setAdapter(tbs: List<Conversation>) {
//        val adapter = ConverstationAdapter(tbs)
 //       rvConv.adapter = adapter
    }

    override fun onClickListener(conversation: Conversation) {
        var fromMail = user.username
        var fromNick = user.nickname
        var fromPfp = user.avatar
        var toMail = conversation.to
        var toNick = conversation.nicknameTo
        var toJob = conversation.jobTo
        var toAvatar = conversation.avatarTo
        var fromJob = user.job
        if (user.username == conversation.to)  {
            toMail = conversation.from
            toNick = conversation.nicknameFrom
            toAvatar = conversation.avatarFrom
            toJob = conversation.jobFrom
        }
        var intent = Intent(requireContext(), ChatActivity::class.java).apply {
            putExtra(ChatActivity.tonick, toNick)
            putExtra(ChatActivity.tomail, toMail)
            putExtra(ChatActivity.tojob, toJob)
            putExtra(ChatActivity.topfp, toAvatar)
            putExtra(ChatActivity.frommail, fromMail)
            putExtra(ChatActivity.fromnick, fromNick)
            putExtra(ChatActivity.frompfp, fromPfp)
            putExtra(ChatActivity.fromjob, fromJob)
        }
        startActivity(intent)
    }


}