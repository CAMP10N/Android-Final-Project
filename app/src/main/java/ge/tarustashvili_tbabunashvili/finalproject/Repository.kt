package ge.tarustashvili_tbabunashvili.finalproject

import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ge.tarustashvili_tbabunashvili.finalproject.data.model.User


class AuthRepository (context: Context/*application: Application*/) {

    //  private lateinit var application: Application
    private var c = context
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var userLiveData: MutableLiveData<FirebaseUser?> = MutableLiveData()
    private var loggedOutLiveData: MutableLiveData<Boolean> = MutableLiveData()
    private val users = Firebase.database.getReference("users")

    init {
        // this.application = application
        if (firebaseAuth.currentUser != null) {
            userLiveData.postValue(firebaseAuth.currentUser)
        }
        loggedOutLiveData.postValue(firebaseAuth.currentUser == null)
    }

    fun getUserLiveData(): MutableLiveData<FirebaseUser?> {
        return userLiveData
    }

    fun getLoggedOutLiveData(): MutableLiveData<Boolean> {
        return loggedOutLiveData
    }


    fun register(nickname: String, password: String, job: String) {
        //var job = findViewById<EditText>(R.id.job).text.toString()
        //progressDialog.show()
        firebaseAuth.createUserWithEmailAndPassword(nickname,password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    userLiveData.postValue(firebaseAuth.currentUser);
                    val newUser = User(username = nickname, job = job)
                    users.child(firebaseAuth.currentUser?.uid!!).setValue(newUser)
                    //startActivity(Intent(this.application.applicationContext, UserActivity::class.java))
                    //finish()
                }   else {
                    //    progressDialog.dismiss()
                    Toast.makeText(c, "You were not registered", Toast.LENGTH_LONG).show()
                    Log.d("er", it.exception.toString())
                }
            }
    }

    fun login(nickname: String, password: String){
        Log.d("haa", "Wtf")
        firebaseAuth.signInWithEmailAndPassword(nickname,password)
            .addOnSuccessListener {
                userLiveData.postValue(firebaseAuth.currentUser)
            }
            .addOnFailureListener {
                Toast.makeText(c, "Log in failed", Toast.LENGTH_LONG).show()
                Log.d("ee", "Erroriaa")
            }
    }

    fun logOut() {
        firebaseAuth.signOut()
        loggedOutLiveData.postValue(true)
    }
}