package ge.tarustashvili_tbabunashvili.finalproject

import android.app.Application
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import ge.tarustashvili_tbabunashvili.finalproject.data.model.Converstation

class UserActivity : AppCompatActivity() {

    val signedInViewModel: SignedInViewModel by lazy {
        ViewModelProvider(this, SignedInViewModelsFactory(application)).get(SignedInViewModel::class.java)
    }

    private lateinit var bottomNavigationView: BottomNavigationView
    private var fragments = arrayListOf(HomeFragment(), SettingsFragment())
    private lateinit var vp: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        signedInViewModel.getLoggedOutLiveData().observe(this, Observer {
            if (it) {
                startActivity(Intent(this, SignInActivity::class.java))
                finish()
            }
        })

        bottomNavigationView = findViewById(R.id.bottom_nav_bar)
        bottomNavigationView.background = null

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
            Log.d("navbar", "clicked")
            when(it.itemId) {
                R.id.home -> vp.currentItem = 0
                R.id.settings -> vp.currentItem = 1
            }
            true
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d("main", "onstart")
    }


    fun logout(view: View) {
        signedInViewModel.logOut()
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