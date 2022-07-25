package ge.tarustashvili_tbabunashvili.finalproject

import android.app.Application
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import ge.tarustashvili_tbabunashvili.finalproject.data.model.User


class UserActivity : AppCompatActivity() {

    val signedInViewModel: SignedInViewModel by lazy {
        ViewModelProvider(this, SignedInViewModelsFactory(application)).get(SignedInViewModel::class.java)
    }

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var bottomAppBar: BottomAppBar
    private var fragments  = arrayListOf(HomeFragment(), SettingsFragment())
    private lateinit var vp: ViewPager2
    private lateinit var currentUser: User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        signedInViewModel.getLoggedOutLiveData().observe(this, Observer {
            if (it) {
                startActivity(Intent(this, SignInActivity::class.java))
                finish()
            }
        })
        signedInViewModel.getCurrentUser().observe(this, Observer {
            if (it != null) {
                currentUser = it
             //   Log.d("useruseruser", it.toString())
            }   else {
             //   Log.d("eeh","eeh")
            }
        })

        bottomNavigationView = findViewById(R.id.bottom_nav_bar)
        bottomNavigationView.background = null

        bottomAppBar = findViewById(R.id.bottom_appbar)

        vp = findViewById(R.id.user_body)
        vp.adapter = UserPagerAdapter(this, fragments)

        val navView = findViewById<BottomNavigationView>(R.id.bottom_nav_bar)

        vp.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                when (position){
                    0 -> navView.selectedItemId = R.id.home
                    1 -> navView.selectedItemId = R.id.settings
                }
            }
        })

        navView.setOnItemSelectedListener {
        //    Log.d("navbar", "clicked")
            when(it.itemId) {
                R.id.home -> vp.currentItem = 0
                R.id.settings -> vp.currentItem = 1
            }
            true
        }
    }
/*
    private val imageUploaded = 100

    fun uploadImage(view: View) {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, imageUploaded)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK && requestCode == imageUploaded) {
            var imageUri = data?.data
            var user = (fragments[1] as SettingsFragment).getUpdatedInfo()
            signedInViewModel.uploadImage(User(username = user.username, nickname = user.nickname, job = user.job), imageUri!!)
        }
    }*/

    override fun onStart() {
        super.onStart()
      //  Log.d("main", "onstart")
    }

    fun onSearchClick(view: View) {
     //   Log.d("user in user", currentUser.username ?: "nothing easy")
        var intent = Intent(this, SearchActivity::class.java).apply {
            putExtra(SearchActivity.myn, currentUser.nickname)
            putExtra(SearchActivity.myj, currentUser.job)
            putExtra(SearchActivity.myun, currentUser.username)
            putExtra(SearchActivity.mya, currentUser.avatar)
        }
        startActivity(intent)

    }


    /* fun logout(view: View) {
         signedInViewModel.logOut()
     }*/
/*
    fun onUpdate(view: View) {
        var user = (fragments[1] as SettingsFragment).getUpdatedInfo()
        signedInViewModel.updateInfo(user)
    }*/

}


class SignedInViewModelsFactory(var application: Application) : ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignedInViewModel::class.java)) {
            return SignedInViewModel(Repository(this.application.applicationContext)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
