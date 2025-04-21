package tr.com.example.performanstakip

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import tr.com.example.performanstakip.databinding.ActivityOdevKontrolBinding

class OdevKontrolActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOdevKontrolBinding
    private lateinit var odevAdapter: OdevAdapter
    private lateinit var studentsList: List<Student>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOdevKontrolBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Class adı alınıyor
        val className = intent.getStringExtra("CLASS_NAME") ?: ""

        // Öğrencilerin listesi (Firestore'dan da alınabilir)
        studentsList = getStudents()

        // Adapter'ı oluşturuyoruz
        odevAdapter = OdevAdapter(studentsList) { student, isChecked ->
            student.isOdevDone = isChecked
        }

        // RecyclerView ile öğrenciler listeleniyor
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = odevAdapter

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

        students.forEach { student ->
            val studentData = hashMapOf(
                "name" to student.name,
                "number" to student.number,
                "odev_done" to student.isOdevDone
            )

            db.collection("kontroller")
                .document(className)
                .collection("ogrenciler")
                .document(student.name)
                .set(studentData, com.google.firebase.firestore.SetOptions.merge())
                .addOnSuccessListener {
                    Toast.makeText(this, "Veriler başarıyla kaydedildi", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Hata: ${exception.message}", Toast.LENGTH_LONG).show()
                }
        }
    }
}
