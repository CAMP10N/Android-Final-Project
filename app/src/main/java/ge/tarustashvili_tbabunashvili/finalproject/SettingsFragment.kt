package ge.tarustashvili_tbabunashvili.finalproject

import android.graphics.drawable.Drawable
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import ge.tarustashvili_tbabunashvili.finalproject.databinding.UserSettingsFragmentBinding

class SettingsFragment: Fragment() {
    private var _binding: UserSettingsFragmentBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var image: ImageView
    private lateinit var username: TextView
    private lateinit var profession: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("home", "oncreate")
        _binding = UserSettingsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.image = view.findViewById(R.id.pfp)
        this.username = view.findViewById(R.id.nm)
        this.profession = view.findViewById(R.id.pos)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}