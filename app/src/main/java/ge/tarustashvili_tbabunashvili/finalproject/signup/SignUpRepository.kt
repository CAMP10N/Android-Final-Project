package ge.tarustashvili_tbabunashvili.finalproject.signup

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ge.tarustashvili_tbabunashvili.finalproject.data.model.User

class SignUpRepository (context: Context/*application: Application*/) {

    private var c = context
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var userLiveData: MutableLiveData<FirebaseUser?> = MutableLiveData()
    private val users = Firebase.database.getReference("users")

    fun getUserLiveData(): MutableLiveData<FirebaseUser?> {
        return userLiveData
    }

    fun register(nickname: String, password: String, job: String) {
        firebaseAuth.createUserWithEmailAndPassword(nickname,password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    userLiveData.postValue(firebaseAuth.currentUser);
                    val newUser = User(username = nickname, nickname = nickname.substringBefore("@android.com"), job = job)
                    users.child(firebaseAuth.currentUser?.uid!!).setValue(newUser)
                        .addOnCompleteListener {
                        }
                        .addOnFailureListener {
                            Toast.makeText(c, "Error while retrieving data!", Toast.LENGTH_SHORT).show()
                            Log.e("Exception from server", it.toString())
                        }
                }   else {
                    Toast.makeText(c, "You were not registered", Toast.LENGTH_LONG).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(c, "Failed to register!", Toast.LENGTH_SHORT).show()
                Log.e("Exception from server", it.toString())
            }
    }
}