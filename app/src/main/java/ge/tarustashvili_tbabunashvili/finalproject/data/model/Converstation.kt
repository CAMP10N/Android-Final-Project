package ge.tarustashvili_tbabunashvili.finalproject.data.model

import android.graphics.drawable.Drawable
import java.util.*

data class Converstation(val photo: Drawable, val name: String,
                         val last_message: String, val date: Date)
