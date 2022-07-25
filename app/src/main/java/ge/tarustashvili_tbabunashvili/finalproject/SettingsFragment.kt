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
import android.widget.Button
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
    private lateinit var updateButton: Button
    private lateinit var signOutButton: Button
    val signedInViewModel: SignedInViewModel by lazy {
        ViewModelProvider(requireActivity(), SignedInViewModelsFactory(requireActivity().application)).get(SignedInViewModel::class.java)
    }
    private var professionData = ""
    private var nicknameData = ""
    private var usernameData = ""
    private var avatarData = ""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       // Log.d("home", "oncreate")
        _binding = UserSettingsFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.image = view.findViewById(R.id.pfp)
        this.username = view.findViewById(R.id.nm)
        this.profession = view.findViewById(R.id.pos)

        this.image.setOnClickListener {
            uploadImage(it)
        }

        this.updateButton = view.findViewById(R.id.update_button)
        this.updateButton.setOnClickListener {
            onUpdate(it)
        }
        this.signOutButton = view.findViewById(R.id.logout_button)
        this.signOutButton.setOnClickListener {
            logout(it)
        }
        signedInViewModel.getCurrentUser().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                setData(it)
                    //  Log.d("ha???", "sadas")
            }   else {
                //Log.d("eeh","eeh")
            }
        })
       /* this.username.text = nicknameData
        this.profession.text = professionData*/
        /*if (avatarData != "") {
            Glide.with(this)
                .load(avatarData)
                .circleCrop()
                .into(this.image)
        }*/
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun setData(user: User) {
        nicknameData = user.nickname ?: ""
        professionData = user.job ?: ""
        usernameData = user.username ?: ""
        avatarData = user.avatar ?: ""
    //    Log.d("wtftft", user.avatar.toString())
        if (view != null) {
            this.username.text = nicknameData
            this.profession.text = professionData
            if (avatarData != "") {
                Glide.with(this)
                    .load(avatarData)
                    .circleCrop()
                    .into(this.image)
            }
        }
    }



    private val imageUploaded = 100

    fun uploadImage(view: View) {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, imageUploaded)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK && requestCode == imageUploaded) {
            var imageUri = data?.data
            var user = getUpdatedInfo()
            signedInViewModel.uploadImage(User(username = user.username, nickname = user.nickname, job = user.job), imageUri!!)
        }
    }

    /*fun onUpdate(view: View) {
        var user = getUpdatedInfo()
        this.signedInViewModel.updateInfo(user)
    }*/

    fun getUpdatedInfo(): User {
        return User(username = this.usernameData, nickname = this.username.text.toString(), job = this.profession.text.toString(), avatar = this.avatarData)
    }

    fun logout(view: View) {
        signedInViewModel.logOut()
    }

    fun onUpdate(view: View) {
        var user = getUpdatedInfo()
        this.signedInViewModel.updateInfo(user)
    }

}