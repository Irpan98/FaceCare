package id.itborneo.facecare.utils.validation

import android.content.Context
import android.graphics.PorterDuff
import android.view.Gravity
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import id.itborneo.facecare.R


@Suppress("DEPRECATION")
class NullChecker(private val context: Context) {

    //return true if null
    fun isInputValid(
        editText: EditText,
        warningText: String
    ): Boolean {

        return if (editText.text.isEmpty()) {
            val toast = Toast.makeText(context, warningText, Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.TOP, 0, 20)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                toast.view?.background
                    ?.setColorFilter(context.getColor(R.color.main_yellow), PorterDuff.Mode.SRC_IN)
                val text: TextView? = toast.view?.findViewById(android.R.id.message)
                text?.setTextColor(context.getColor(R.color.white))
            }
            toast.show()

            false
        } else {
            editText.error = null
            true

        }


    }
}