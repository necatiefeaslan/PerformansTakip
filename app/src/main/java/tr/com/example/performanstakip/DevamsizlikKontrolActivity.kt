package tr.com.example.performanstakip

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import tr.com.example.performanstakip.databinding.ActivityDevamsizlikKontrolBinding

class DevamsizlikKontrolActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDevamsizlikKontrolBinding
    private lateinit var studentAdapter: StudentAdapter
    private lateinit var studentsList: List<Student>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDevamsizlikKontrolBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Class adı alınıyor
        val className = intent.getStringExtra("CLASS_NAME") ?: ""

        // Öğrencilerin listesi (Firestore'dan da alınabilir)
        studentsList = getStudents()

        studentAdapter = StudentAdapter(studentsList) { student, isChecked ->
            student.isDevamsizlikDone = isChecked
        }

        // RecyclerView ile öğrenciler listeleniyor
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = studentAdapter

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

                "devamsizlik" to student.isDevamsizlikDone
            )

            db.collection("kontroller")
                .document(className) // Sınıf adıyla belgenin referansı
                .collection("ogrenciler") // Öğrencilerin koleksiyonu
                .document(student.name) // Öğrenci numarasını benzersiz ID olarak kullanıyoruz
                .set(
                    studentData,
                    com.google.firebase.firestore.SetOptions.merge()
                ) // Merge işlemi ile eski veriyi silmeden ekleme
                .addOnSuccessListener {
                    // Başarı durumunda işlemi sonlandırıyoruz
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Hata: ${exception.message}", Toast.LENGTH_LONG).show()
                }
        }

        // Veriler başarıyla kaydedildiyse
        Toast.makeText(this, "Veriler başarıyla kaydedildi", Toast.LENGTH_SHORT).show()
        finish()
    }
}
