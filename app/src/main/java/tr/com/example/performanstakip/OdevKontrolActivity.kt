package tr.com.example.performanstakip

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import tr.com.example.performanstakip.databinding.ActivityOdevKontrolBinding

class OdevKontrolActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOdevKontrolBinding
    private lateinit var studentAdapter: StudentAdapter
    private lateinit var studentsList: List<Student>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOdevKontrolBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Class ve Öğrenci adı alınıyor
        val className = intent.getStringExtra("CLASS_NAME") ?: ""
        val studentName = intent.getStringExtra("STUDENT_NAME") ?: "Bilinmeyen Öğrenci"

        // Öğrencilerin listesi (Firestore'dan da alınabilir)
        studentsList = getStudents()

        studentAdapter = StudentAdapter(
            studentsList,
            { student, isChecked ->
                student.isOdevDone = isChecked
            }
        )


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

        // Tüm öğrenciler için işlemi başlatıyoruz
        var isSuccess = true

        students.forEach { student ->
            val studentData = hashMapOf(
                "name" to student.name,
                "number" to student.number,
                "odev" to student.isOdevDone,
                "kiyafet" to student.isKiyafetDone,
                "devamsizlik" to student.isDevamsizlikDone
            )

            db.collection("kontroller")
                .document(className) // Sınıf adıyla belgenin referansı
                .collection("ogrenciler") // Öğrencilerin koleksiyonu
                .document(student.name.toString()) // Öğrenci numarasını benzersiz ID olarak kullanıyoruz
                .set(
                    studentData,
                    com.google.firebase.firestore.SetOptions.merge()
                ) // Merge işlemi ile eski veriyi silmeden ekleme
                .addOnSuccessListener {
                    // Başarı durumunda flag'i true yapıyoruz
                    isSuccess = isSuccess && true
                }
                .addOnFailureListener { exception ->
                    // Hata durumunda flag'i false yapıyoruz
                    isSuccess = false
                    Toast.makeText(this, "Hata: ${exception.message}", Toast.LENGTH_LONG).show()
                }
        }

        // Tüm işlemler tamamlandığında kaydetme işlemini sonlandırıyoruz
        if (isSuccess) {
            Toast.makeText(this, "Veriler başarıyla kaydedildi", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
