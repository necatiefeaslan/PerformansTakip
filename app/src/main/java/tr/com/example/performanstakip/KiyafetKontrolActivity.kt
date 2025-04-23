package tr.com.example.performanstakip

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import tr.com.example.performanstakip.databinding.ActivityKiyafetKontrolBinding
import java.text.SimpleDateFormat
import java.util.*

class KiyafetKontrolActivity : AppCompatActivity() {

    private lateinit var binding: ActivityKiyafetKontrolBinding
    private lateinit var adapter: KiyafetAdapter
    private lateinit var studentsList: List<Student>
    private var saveCount = 0
    private val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKiyafetKontrolBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Class adı alınıyor
        val className = intent.getStringExtra("CLASS_NAME") ?: ""

        // Bugünün tarihini ayarla
        binding.etTarih.setText(dateFormat.format(Date()))

        // Öğrencilerin listesi
        studentsList = getStudents()

        // Adapter'ı oluşturuyoruz
        adapter = KiyafetAdapter(studentsList) { student, isDone ->
            student.isKiyafetDone = isDone
        }

        // RecyclerView ile öğrenciler listeleniyor
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        // Kaydet butonuna tıklama
        binding.btnSave.setOnClickListener {
            if (kontrolNotlarDolu()) {
                saveStudentControls(className)
            } else {
                Toast.makeText(this, "Lütfen tüm öğrencilerin kontrolünü yapınız!", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun getStudents(): List<Student> {
        return listOf(
            Student("Ahmet Yılmaz", 101),
            Student("Mehmet Kaya", 102),
            Student("Ayşe Demir", 103)
        )
    }

    private fun kontrolNotlarDolu(): Boolean {
        return studentsList.all { it.isKiyafetDone != null }
    }

    private fun saveStudentControls(className: String) {
        val db = FirebaseFirestore.getInstance()
        saveCount = 0

        binding.progressBar.visibility = android.view.View.VISIBLE
        binding.btnSave.isEnabled = false

        val currentDate = binding.etTarih.text.toString()

        studentsList.forEach { student ->
            val studentData = hashMapOf(
                "name" to student.name,
                "number" to student.number,
                "isKiyafetDone" to student.isKiyafetDone,
                "date" to currentDate
            )

            db.collection("kiyafetKontrol")
                .document(className)
                .collection("ogrenciler")
                .document(student.name)
                .set(studentData)
                .addOnSuccessListener {
                    saveCount++
                    if (saveCount == studentsList.size) {
                        Toast.makeText(this, "Kıyafet kontrolleri başarıyla kaydedildi", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Hata: ${e.message}", Toast.LENGTH_LONG).show()
                    binding.progressBar.visibility = android.view.View.GONE
                    binding.btnSave.isEnabled = true
                }
        }
    }
}
