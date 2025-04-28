package tr.com.example.performanstakip

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import tr.com.example.performanstakip.databinding.ActivityListBinding

class ListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListBinding

    // Sabit sınıf listesi
    private val classes = listOf(
        SchoolClass("9-A"),
        SchoolClass("9-B"),
        SchoolClass("9-C"),
        SchoolClass("10-A"),
        SchoolClass("10-B"),
        SchoolClass("10-C"),
        SchoolClass("11-A"),
        SchoolClass("11-B"),
        SchoolClass("11-C"),
        SchoolClass("12-A"),
        SchoolClass("12-B"),
        SchoolClass("12-C")
    )

    override fun onCreate(s: Bundle?) {
        super.onCreate(s)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // RecyclerView'ı ayarla
        binding.rvClasses.layoutManager = LinearLayoutManager(this)
        
        // Adapter'ı ayarla
        binding.rvClasses.adapter = ListAdapter(classes) { className ->
            // Sınıf seçildiğinde öğrenci listesine geç
            startActivity(
                Intent(this, StudentListActivity::class.java)
                    .putExtra("CLASS_NAME", className)
            )
        }


    }
}