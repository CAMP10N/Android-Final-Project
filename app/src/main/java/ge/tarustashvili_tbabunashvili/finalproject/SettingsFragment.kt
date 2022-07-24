package ge.tarustashvili_tbabunashvili.finalproject

import android.app.Application
import android.content.Intent
import android.graphics.drawable.Drawable
import android.media.Image
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import ge.tarustashvili_tbabunashvili.finalproject.data.model.User
import ge.tarustashvili_tbabunashvili.finalproject.databinding.UserSettingsFragmentBinding

class SettingsFragment(): Fragment() {
    private var _binding: UserSettingsFragmentBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var image: ImageView
    private lateinit var username: TextView
    private lateinit var profession: TextView
    private var professionData = ""
    private var nicknameData = ""
    private var usernameData = ""
    private var avatarData = ""
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
        this.username.text = nicknameData
        this.profession.text = professionData
        if (avatarData != "") {
            Glide.with(this)
                .load(avatarData)
                .circleCrop()
                .into(this.image)
        }
    }

    private val imageUploaded = 100


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun setData(user: User) {
        nicknameData = user.nickname ?: ""
        professionData = user.job ?: ""
        usernameData = user.username ?: ""
        avatarData = user.avatar ?: ""
        Log.d("wtftft", user.avatar.toString())
        if (view != null) {
            if (avatarData != "") {
                Glide.with(this)
                    .load(avatarData)
                    .circleCrop()
                    .into(this.image)
            }
        }
    }

    fun getUpdatedInfo(): User {

        return User(username = this.usernameData, nickname = this.username.text.toString(), job = this.profession.text.toString(), avatar = this.avatarData)
    }

}