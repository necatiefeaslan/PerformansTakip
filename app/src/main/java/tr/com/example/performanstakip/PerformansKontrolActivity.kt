package tr.com.example.performanstakip

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import tr.com.example.performanstakip.databinding.ActivityPerformansKontrolBinding
import java.text.SimpleDateFormat
import java.util.*

class PerformansKontrolActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPerformansKontrolBinding
    private lateinit var adapter: PerformansAdapter
    private lateinit var studentsList: List<Student>
    private var saveCount = 0
    private val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPerformansKontrolBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            finish()
        }

        // Class adı alınıyor
        val className = intent.getStringExtra("CLASS_NAME") ?: ""

        // Bugünün tarihini ayarla
        binding.etTarih.setText(dateFormat.format(Date()))

        // Tarih seçici tıklaması
        binding.etTarih.setOnClickListener {
            showDatePicker()
        }

        // Öğrencilerin listesi
        studentsList = getStudents()

        // Adapter'ı oluşturuyoruz
        adapter = PerformansAdapter(studentsList) { student, performansNot ->
            student.performansNot = performansNot
        }

        // RecyclerView ile öğrenciler listeleniyor
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        // Kaydet butonuna tıklama
        binding.btnSave.setOnClickListener {
            if (kontrolNotlarDolu()) {
                saveStudentControls(className)
            } else {
                Toast.makeText(this, "Lütfen tüm öğrencilerin notlarını giriniz!", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            this,
            { _, year, month, day ->
                calendar.set(year, month, day)
                binding.etTarih.setText(dateFormat.format(calendar.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun getStudents(): List<Student> {
        return listOf(
            Student("Ahmet Yılmaz", 101),
            Student("Mehmet Kaya", 102),
            Student("Ayşe Demir", 103)
        )
    }

    private fun kontrolNotlarDolu(): Boolean {
        return studentsList.all { it.performansNot != null }
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
                "performansNot" to student.performansNot,
                "date" to currentDate
            )

            db.collection("performansKontrol")
                .document(className)
                .collection("ogrenciler")
                .document(student.name)
                .set(studentData)
                .addOnSuccessListener {
                    saveCount++
                    if (saveCount == studentsList.size) {
                        Toast.makeText(this, "Performans notları başarıyla kaydedildi", Toast.LENGTH_SHORT).show()
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

