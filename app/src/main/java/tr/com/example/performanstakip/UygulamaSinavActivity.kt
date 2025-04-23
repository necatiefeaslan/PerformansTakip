package tr.com.example.performanstakip

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import tr.com.example.performanstakip.databinding.ActivityUygulamaSinavBinding
import java.text.SimpleDateFormat
import java.util.*

class UygulamaSinavActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUygulamaSinavBinding
    private lateinit var adapter: UygulamaSinavAdapter
    private lateinit var studentsList: List<Student>
    private var saveCount = 0
    private val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUygulamaSinavBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val className = intent.getStringExtra("CLASS_NAME") ?: ""
        title = "$className - Uygulama Sınavı"

        // Tarihi bugünün tarihi olarak ayarla
        binding.etTarih.setText(dateFormat.format(Date()))

        // Tarih seçici açılır
        binding.etTarih.setOnClickListener { showDatePicker() }

        // Öğrencileri yükle
        studentsList = getStudents()

        adapter = UygulamaSinavAdapter(studentsList) { student, grade ->
            student.grade = grade
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        // Kaydet butonuna basılınca
        binding.btnSave.setOnClickListener {
            if (kontrolNotlarDolu()) {
                saveStudentGrades(className)
            } else {
                Toast.makeText(
                    this,
                    "Lütfen tüm öğrencilerin notlarını giriniz!",
                    Toast.LENGTH_LONG
                ).show()
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
        return studentsList.all { it.grade != null }
    }

    private fun saveStudentGrades(className: String) {
        val db = FirebaseFirestore.getInstance()
        saveCount = 0

        val sinavAdi = binding.etSinavAdi.text.toString().trim()
        val sinavTarihi = binding.etTarih.text.toString()

        if (sinavAdi.isEmpty()) {
            binding.etSinavAdi.error = "Sınav adı giriniz"
            return
        }

        binding.progressBar.visibility = android.view.View.VISIBLE
        binding.btnSave.isEnabled = false

        studentsList.forEach { student ->
            val studentData = hashMapOf(
                "name" to student.name,
                "number" to student.number,
                "grade" to student.grade,
                "examName" to sinavAdi,
                "date" to sinavTarihi
            )

            db.collection("uygulamaSinavlari")
                .document(className)
                .collection("ogrenciler")
                .document(student.name) // Burada student.name kullanılıyor
                .set(studentData, SetOptions.merge())
                .addOnSuccessListener {
                    saveCount++
                    if (saveCount == studentsList.size) {
                        Toast.makeText(this, "Notlar başarıyla kaydedildi", Toast.LENGTH_SHORT)
                            .show()
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
}

