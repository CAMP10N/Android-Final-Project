package ge.tarustashvili_tbabunashvili.finalproject.user

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import ge.tarustashvili_tbabunashvili.finalproject.R
import ge.tarustashvili_tbabunashvili.finalproject.data.model.User
import ge.tarustashvili_tbabunashvili.finalproject.search.SearchActivity
import ge.tarustashvili_tbabunashvili.finalproject.signin.Repository
import ge.tarustashvili_tbabunashvili.finalproject.signin.SignInActivity


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
            when(it.itemId) {
                R.id.home -> vp.currentItem = 0
                R.id.settings -> vp.currentItem = 1
            }
            true
        }
    }

    override fun onStart() {
        super.onStart()
    }

    fun onSearchClick(view: View) {
        var intent = Intent(this, SearchActivity::class.java).apply {
            putExtra(SearchActivity.myn, currentUser.nickname)
            putExtra(SearchActivity.myj, currentUser.job)
            putExtra(SearchActivity.myun, currentUser.username)
            putExtra(SearchActivity.mya, currentUser.avatar)
        }
        startActivity(intent)

    }

}


class SignedInViewModelsFactory(var application: Application) : ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignedInViewModel::class.java)) {
            return SignedInViewModel(Repository(this.application.applicationContext)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
