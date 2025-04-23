package tr.com.example.performanstakip

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import tr.com.example.performanstakip.databinding.ActivityClassListBinding

class ClassListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityClassListBinding
    private lateinit var classAdapter: ClassAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClassListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Örnek sınıf verileri
        val classList = listOf("9-A", "9-B", "9-C", "10-A", "10-B", "10-C", "11-A", "11-B", "11-C", "12-A", "12-B", "12-C")

        val studentName = intent.getStringExtra("STUDENT_NAME") ?: "Bilinmeyen Öğrenci"

        // RecyclerView ayarları
        binding.rvClassList.layoutManager = LinearLayoutManager(this)
        classAdapter = ClassAdapter(classList) { className ->
            // Tıklanan sınıf ismini öğrenci listesine ilet
            val intent = Intent(this, StudentDetailActivity::class.java)
            intent.putExtra("CLASS_NAME", className) // Seçilen sınıf ismini gönder
            intent.putExtra("STUDENT_NAME", studentName) // Seçilen öğrenci adını gönder
            startActivity(intent)
        }
        binding.rvClassList.adapter = classAdapter
    }
}