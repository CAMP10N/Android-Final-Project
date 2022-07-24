package ge.tarustashvili_tbabunashvili.finalproject

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : AppCompatActivity() {
    private lateinit var progressDialog: ProgressDialog
    private lateinit var firebaseAuth: FirebaseAuth
    private var nickname = ""
    private var password = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait...")
        progressDialog.setMessage("Logging in...")
        progressDialog.setCanceledOnTouchOutside(false)

        firebaseAuth = FirebaseAuth.getInstance()
        checkIfLogged()
    }


    fun checkIfLogged() {
//        val user = firebaseAuth.currentUser
//        if (user != null) {
            startActivity(Intent(this, UserActivity::class.java))
            finish()
//        }
    }
    fun onRegisterClick(view: View) {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }

    fun onLoginClick(view: View) {
        validateData()
    }

    fun validateData(){
        nickname = findViewById<EditText>(R.id.nickname).text.toString()
        password = findViewById<EditText>(R.id.password).text.toString()
        progressDialog.show()
        firebaseAuth.signInWithEmailAndPassword(nickname,password)
            .addOnSuccessListener {
                progressDialog.dismiss()
                startActivity(Intent(this, UserActivity::class.java))
                finish()
            }
            .addOnFailureListener {
                progressDialog.dismiss()
            }
    }


}