package tr.com.example.performanstakip

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import tr.com.example.performanstakip.databinding.ActivityDevamsizlikKontrolBinding

class DevamsizlikKontrolActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDevamsizlikKontrolBinding
    private lateinit var devamsizlikAdapter: DevamsizlikAdapter
    private lateinit var studentsList: List<Student>
    private var saveCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDevamsizlikKontrolBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Class adı alınıyor
        val className = intent.getStringExtra("CLASS_NAME") ?: ""

        // Öğrencilerin listesi (Firestore'dan da alınabilir)
        studentsList = getStudents()

        // Adapter'ı oluşturuyoruz
        devamsizlikAdapter = DevamsizlikAdapter(studentsList) { student, isChecked ->
            student.isDevamsizlikDone = isChecked
        }

        // RecyclerView ile öğrenciler listeleniyor
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = devamsizlikAdapter

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
                "devamsizlik_done" to student.isDevamsizlikDone
            )

            db.collection("kontroller")
                .document(className)
                .collection("ogrenciler")
                .document(student.name)
                .set(studentData, com.google.firebase.firestore.SetOptions.merge())
                .addOnSuccessListener {
                    saveCount++
                    if (saveCount == students.size) {
                        Toast.makeText(this, "Devamsızlık kontrolleri başarıyla kaydedildi", Toast.LENGTH_SHORT).show()
                        finish() // Aktiviteyi kapat
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Hata: ${exception.message}", Toast.LENGTH_LONG).show()
                }
        }
    }
}
