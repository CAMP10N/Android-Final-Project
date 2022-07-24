package ge.tarustashvili_tbabunashvili.finalproject

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import ge.tarustashvili_tbabunashvili.finalproject.data.model.User
import java.util.*


class Repository (context: Context/*application: Application*/) {

    //  private lateinit var application: Application
    private var c = context
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var userLiveData: MutableLiveData<FirebaseUser?> = MutableLiveData()
    private var loggedOutLiveData: MutableLiveData<Boolean> = MutableLiveData()
    private val users = Firebase.database.getReference("users")
    private var currentUser: MutableLiveData<User?> = MutableLiveData()
    init {
        // this.application = application
        if (firebaseAuth.currentUser != null) {
            userLiveData.postValue(firebaseAuth.currentUser)
            users.child(firebaseAuth.currentUser!!.uid).get().addOnSuccessListener {
                currentUser.postValue(User(job = it.child("job").getValue<String>(),
                    nickname = it.child("nickname").getValue<String>(),
                    username = it.child("username").getValue<String>(),
                    avatar = it.child("avatar").getValue<String>()
                ))
                Log.d("ratom", "gamirbixar")

            }.addOnFailureListener {
                Log.d("ratom", "memalebi")
            }
        }
        loggedOutLiveData.postValue(firebaseAuth.currentUser == null)

    }

    fun getUserLiveData(): MutableLiveData<FirebaseUser?> {
        return userLiveData
    }

    fun getLoggedOutLiveData(): MutableLiveData<Boolean> {
        return loggedOutLiveData
    }

    fun getCurrentUser(): MutableLiveData<User?> {
        return currentUser
    }


    fun register(nickname: String, password: String, job: String) {
        //var job = findViewById<EditText>(R.id.job).text.toString()
        //progressDialog.show()
        firebaseAuth.createUserWithEmailAndPassword(nickname,password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    userLiveData.postValue(firebaseAuth.currentUser);
                    val newUser = User(username = nickname, nickname = nickname.substringBefore("@android.com"), job = job)
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
            }
    }

    fun logOut() {
        firebaseAuth.signOut()
        loggedOutLiveData.postValue(true)
        currentUser.postValue(null)
    }

    fun updateInfo(user: User) {
        users.child(firebaseAuth.currentUser?.uid!!).setValue(user)
        currentUser.postValue(user)
        val signedUser = firebaseAuth.currentUser
        users.child(firebaseAuth.currentUser?.uid!!).setValue(user)
    }

    fun uploadImage(user: User, uri: Uri) {
        val storageRef = Firebase.storage.reference
        val imageRef = storageRef.child("images/" + UUID.randomUUID().toString());
        var uploadTask = imageRef.putFile(uri!!)
        val urlTask = uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            imageRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                updateInfo(User(username = user.username, nickname = user.nickname, job = user.job, avatar = downloadUri.toString()))
                //presenter.imageUploaded(downloadUri.toString())
            }
        }
    }
}