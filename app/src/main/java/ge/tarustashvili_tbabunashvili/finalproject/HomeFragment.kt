package ge.tarustashvili_tbabunashvili.finalproject

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import ge.tarustashvili_tbabunashvili.finalproject.data.model.Conversation
import ge.tarustashvili_tbabunashvili.finalproject.data.model.Message
import ge.tarustashvili_tbabunashvili.finalproject.data.model.User
import ge.tarustashvili_tbabunashvili.finalproject.databinding.HomePageFragmentBinding

class HomeFragment: Fragment() {

    private var _binding: HomePageFragmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var rvConv: RecyclerView

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
        rvConv.adapter = ConversationAdapter(emptyList(), requireContext(),"")
        signedInViewModel.getCurrentUser().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                chatViewModel.registerConversationListener(it.username?:"")
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
    }

    fun updateData(conversations: MutableList<Conversation>)   {
       // Log.d("shemovida?", " upalo")
       // Log.d("aba raga ginda", conversations.toString())
        (rvConv.adapter as ConversationAdapter).items = conversations
        (rvConv.adapter as ConversationAdapter).notifyDataSetChanged()
    }

    private fun setAdapter(tbs: List<Conversation>) {
//        val adapter = ConverstationAdapter(tbs)
 //       rvConv.adapter = adapter
    }



}