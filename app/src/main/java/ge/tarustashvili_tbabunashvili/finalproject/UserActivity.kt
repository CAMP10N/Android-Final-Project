package ge.tarustashvili_tbabunashvili.finalproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import ge.tarustashvili_tbabunashvili.finalproject.data.model.Converstation

class UserActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private var fragments = arrayListOf(HomeFragment(), SettingsFragment())
    private lateinit var vp: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
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


}