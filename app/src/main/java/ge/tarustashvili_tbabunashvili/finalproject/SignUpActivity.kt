package ge.tarustashvili_tbabunashvili.finalproject

import android.app.Application
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog

    val signInAndUpViewModel: SignInAndUpViewModel by lazy {
        ViewModelProvider(this, MainViewModelsFactory(application)).get(SignInAndUpViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        signInAndUpViewModel.getUserLiveData().observe(this, Observer {
            if (it != null) {
                startActivity(Intent(this, UserActivity::class.java))
                finish()
            }
        })
        /*firebaseAuth = FirebaseAuth.getInstance()
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait...")
        progressDialog.setMessage("Signing up...")
        progressDialog.setCanceledOnTouchOutside(false)*/

    }

    companion object{
        const val User = "id"
    }
    fun register(view: View) {
        val nickname = findViewById<EditText>(R.id.nickname).text.toString()
        val password = findViewById<EditText>(R.id.password).text.toString()
        val job = findViewById<EditText>(R.id.job).text.toString()
        if (nickname.isEmpty()) {
            Toast.makeText(this, "You have to fill in nickname!", Toast.LENGTH_SHORT).show()
            return
        }
        if (password.length < 6) {
            Toast.makeText(this, "Password must be at least 6 characters long!", Toast.LENGTH_SHORT).show()
            return
        }
        if (job.isEmpty()) {
            Toast.makeText(this, "You have to fill in job field!", Toast.LENGTH_SHORT).show()
            return
        }
        signInAndUpViewModel.register(nickname.fakeMail(),password, job)
    }
}