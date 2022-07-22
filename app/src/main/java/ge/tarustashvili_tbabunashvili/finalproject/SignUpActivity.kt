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
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import ge.tarustashvili_tbabunashvili.finalproject.data.UsersDB
import ge.tarustashvili_tbabunashvili.finalproject.data.entity.User

class SignUpActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog

    val viewModel: ViewModel by lazy {
        ViewModelProvider(this, MainViewModelsFactory(application)).get(ViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        firebaseAuth = FirebaseAuth.getInstance()
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait...")
        progressDialog.setMessage("Signing up...")
        progressDialog.setCanceledOnTouchOutside(false)

    }

    companion object{
        const val User = "id"
    }
    fun register(view: View) {
        var nickname = findViewById<EditText>(R.id.nickname).text.toString()
        var password = findViewById<EditText>(R.id.password).text.toString()
        var job = findViewById<EditText>(R.id.job).text.toString()
        progressDialog.show()
        firebaseAuth.createUserWithEmailAndPassword(nickname,password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    progressDialog.dismiss()
                    startActivity(Intent(this, UserActivity::class.java))
                    finish()
                }   else {
                    progressDialog.dismiss()
                    Toast.makeText(this, "You were not registered", Toast.LENGTH_LONG).show()
                    Log.e("Error", "Error occurred while trying to register user", it.exception)
                }
            }
    }
}


class MainViewModelsFactory(var application: Application) : ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewModel::class.java)) {
            return ViewModel(
                Repository(
                    Room.databaseBuilder(application, UsersDB::class.java, "Database").build()
                )
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }


}