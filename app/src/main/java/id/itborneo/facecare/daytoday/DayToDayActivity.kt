package id.itborneo.facecare.daytoday

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import id.itborneo.facecare.core.local.AppDatabase
import id.itborneo.facecare.databinding.ActivityDayToDayBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
    private lateinit var adapter: DayToDayAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        initRecycler()
        observerData()
    }


    private fun initBinding() {
        binding = ActivityDayToDayBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }


    private fun initRecycler() {
        binding.rvArticle.layoutManager = LinearLayoutManager(this)
        adapter = DayToDayAdapter {
//            actionToArticle(it)
        }
        binding.rvArticle.adapter = adapter
    }

    private fun observerData() {
        val dao = AppDatabase.getInstance(this).resultDao()

        GlobalScope.launch(Dispatchers.IO) {
            val results = dao.getResults()

            withContext(Dispatchers.Main) {
                adapter.set(results)
            }

        }

    }
}