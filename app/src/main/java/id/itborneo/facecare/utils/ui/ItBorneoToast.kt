package id.itborneo.facecare.utils.ui

import android.content.Context
import android.graphics.PorterDuff
import android.view.Gravity
import android.widget.TextView
import android.widget.Toast
import id.itborneo.facecare.R

object ItBorneoToast {

    fun toastMain(context: Context, text: String) {
        val toast = Toast.makeText(context, text, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.TOP, 0, 20)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            toast.view?.background
                ?.setColorFilter(context.getColor(R.color.main_yellow), PorterDuff.Mode.SRC_IN)
            val text: TextView? = toast.view?.findViewById(android.R.id.message)
            text?.setTextColor(context.getColor(R.color.white))
        }
        toast.show()
    }
}