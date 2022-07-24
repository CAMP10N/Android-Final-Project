package ge.tarustashvili_tbabunashvili.finalproject

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView

class ChatActivity: AppCompatActivity() {

    val signedInViewModel: SignedInViewModel by lazy {
        ViewModelProvider(this, SignedInViewModelsFactory(application)).get(SignedInViewModel::class.java)
    }

    private lateinit var message: EditText

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_chat)
        signedInViewModel.getLoggedOutLiveData().observe(this, Observer {
            if (it) {
                startActivity(Intent(this, SignInActivity::class.java))
                finish()
            }
        })
    }

    override fun onStart() {
        super.onStart()
        Log.d("main", "onstart")
    }
}