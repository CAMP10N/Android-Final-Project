package ge.tarustashvili_tbabunashvili.finalproject.data.model


import com.google.firebase.database.IgnoreExtraProperties


@IgnoreExtraProperties
data class User(
    var userId: String? = null,
    val username: String? = null,
    val nickname: String? = null,
    val job: String? = null,
    val avatar: String? = null
)