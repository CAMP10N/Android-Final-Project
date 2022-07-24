package ge.tarustashvili_tbabunashvili.finalproject

import android.app.Application
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : AppCompatActivity() {

    val signInAndUpViewModel: SignInAndUpViewModel by lazy {
        ViewModelProvider(this, MainViewModelsFactory(application)).get(SignInAndUpViewModel::class.java)
    }

    //private val signInAndUpViewModel = ViewModelProvider.AndroidViewModelFactory(application).create(SignInAndUpViewModel::class.java)
    // private val signInAndUpViewModel = ViewModelProvider.AndroidViewModelFactory(application).create(v::class.java)
    private lateinit var progressDialog: ProgressDialog
    private lateinit var firebaseAuth: FirebaseAuth
    private var nickname = ""
    private var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        signInAndUpViewModel.getUserLiveData().observe(this, Observer {
            if (it != null) {
                startActivity(Intent(this, UserActivity::class.java))
                finish()
            }
        })
        /*
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait...")
        progressDialog.setMessage("Logging in...")
        progressDialog.setCanceledOnTouchOutside(false)*/

        /*     firebaseAuth = FirebaseAuth.getInstance()
             checkIfLogged()*/
    }


    /*fun checkIfLogged() {
        val user = firebaseAuth.currentUser
        if (user != null) {
            startActivity(Intent(this, UserActivity::class.java))
            finish()
        }
    }*/

    fun onRegisterClick(view: View) {
        startActivity(Intent(this, SignUpActivity::class.java))
    }

    fun onLoginClick(view: View) {
        validateData()
    }

    fun validateData(){
        nickname = findViewById<EditText>(R.id.nickname).text.toString()
        password = findViewById<EditText>(R.id.password).text.toString()
        if (nickname.isEmpty()) {
            Toast.makeText(this, "You have to fill in nickname!", Toast.LENGTH_SHORT).show()
            return
        }
        if (password.length < 6) {
            Toast.makeText(this, "Password must be at least 6 characters long!", Toast.LENGTH_SHORT).show()
            return
        }
        signInAndUpViewModel.login(nickname.fakeMail(),password)
    }


}


fun String.fakeMail(): String {
    return "$this@android.com"
}


class MainViewModelsFactory(var application: Application) : ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignInAndUpViewModel::class.java)) {
            return SignInAndUpViewModel(Repository(this.application.applicationContext)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}