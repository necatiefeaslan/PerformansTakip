package tr.com.example.performanstakip

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import tr.com.example.performanstakip.databinding.ActivityKiyafetKontrolBinding

class KiyafetKontrolActivity : AppCompatActivity() {

    private lateinit var binding: ActivityKiyafetKontrolBinding
    private lateinit var kiyafetAdapter: KiyafetAdapter
    private lateinit var studentsList: List<Student>
    private var saveCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKiyafetKontrolBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Class adı alınıyor
        val className = intent.getStringExtra("CLASS_NAME") ?: ""

        // Öğrencilerin listesi (Firestore'dan da alınabilir)
        studentsList = getStudents()

        // Adapter'ı oluşturuyoruz
        kiyafetAdapter = KiyafetAdapter(studentsList) { student, isChecked ->
            student.isKiyafetDone = isChecked
        }

        // RecyclerView ile öğrenciler listeleniyor
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = kiyafetAdapter

        // Kaydet butonuna tıklama
        binding.btnSave.setOnClickListener {
            saveStudentControls(className, studentsList)
        }
    }

    private fun getStudents(): List<Student> {
        // Öğrencilerin listesi Firestore'dan alınabilir
        return listOf(
            Student("Ahmet Yılmaz", 123),
            Student("Mehmet Kaya", 124),
            Student("Ayşe Demir", 125)
        )
    }

    private fun saveStudentControls(className: String, students: List<Student>) {
        val db = FirebaseFirestore.getInstance()
        saveCount = 0

        students.forEach { student ->
            val studentData = hashMapOf(
                "name" to student.name,
                "number" to student.number,
                "kiyafet_done" to student.isKiyafetDone
            )

            db.collection("kontroller")
                .document(className)
                .collection("ogrenciler")
                .document(student.name)
                .set(studentData, com.google.firebase.firestore.SetOptions.merge())
                .addOnSuccessListener {
                    saveCount++
                    if (saveCount == students.size) {
                        Toast.makeText(this, "Kıyafet kontrolleri başarıyla kaydedildi", Toast.LENGTH_SHORT).show()
                        finish() // Aktiviteyi kapat
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Hata: ${exception.message}", Toast.LENGTH_LONG).show()
                }
        }
    }
}
