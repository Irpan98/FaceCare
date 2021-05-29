package id.itborneo.facecare.daytoday

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import id.itborneo.facecare.databinding.ActivityDayToDayBinding

class DayToDayActivity : AppCompatActivity() {


    companion object {
        private const val TAG = "DayToDayActivity"

        fun getInstance(context: Context) {
            val intent = Intent(context, DayToDayActivity::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            context.startActivity(intent)
        }
    }

    private lateinit var binding: ActivityDayToDayBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
    }

    private fun initBinding() {
        binding = ActivityDayToDayBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}