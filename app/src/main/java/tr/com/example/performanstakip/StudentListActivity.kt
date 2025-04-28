package tr.com.example.performanstakip

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import tr.com.example.performanstakip.databinding.ActivityStudentListBinding

class StudentListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStudentListBinding
    private val db = FirebaseFirestore.getInstance()

    // Sabit öğrenci listesi - sınıflara göre
    private val studentsByClass = mapOf(
        "9-A" to listOf(
            StudentResult("Ahmet Yılmaz", 101),
            StudentResult("Ayşe Demir", 102),
            StudentResult("Mehmet Kaya", 103)
        ),
        "9-B" to listOf(
            StudentResult("Ahmet Yılmaz", 101),
            StudentResult("Ayşe Demir", 102),
            StudentResult("Mehmet Kaya", 103)
        ),
        "9-C" to listOf(
            StudentResult("Ahmet Yılmaz", 101),
            StudentResult("Ayşe Demir", 102),
            StudentResult("Mehmet Kaya", 103)
        ),
        "10-A" to listOf(
            StudentResult("Ahmet Yılmaz", 101),
            StudentResult("Ayşe Demir", 102),
            StudentResult("Mehmet Kaya", 103)
        ),
        "10-B" to listOf(
            StudentResult("Ahmet Yılmaz", 101),
            StudentResult("Ayşe Demir", 102),
            StudentResult("Mehmet Kaya", 103)
        ),
        "10-C" to listOf(
            StudentResult("Ahmet Yılmaz", 101),
            StudentResult("Ayşe Demir", 102),
            StudentResult("Mehmet Kaya", 103)
        ),
        "11-A" to listOf(
            StudentResult("Ahmet Yılmaz", 101),
            StudentResult("Ayşe Demir", 102),
            StudentResult("Mehmet Kaya", 103)
        ),
        "11-B" to listOf(
            StudentResult("Ahmet Yılmaz", 101),
            StudentResult("Ayşe Demir", 102),
            StudentResult("Mehmet Kaya", 103)
        ),
        "11-C" to listOf(
            StudentResult("Ahmet Yılmaz", 101),
            StudentResult("Ayşe Demir", 102),
            StudentResult("Mehmet Kaya", 103)
        ),
        "12-A" to listOf(
            StudentResult("Ahmet Yılmaz", 101),
            StudentResult("Ayşe Demir", 102),
            StudentResult("Mehmet Kaya", 103)
        ),
        "12-B" to listOf(
            StudentResult("Ahmet Yılmaz", 101),
            StudentResult("Ayşe Demir", 102),
            StudentResult("Mehmet Kaya", 103)
        ),
        "12-C" to listOf(
            StudentResult("Ahmet Yılmaz", 101),
            StudentResult("Ayşe Demir", 102),
            StudentResult("Mehmet Kaya", 103)
        )
    )

    override fun onCreate(s: Bundle?) {
        super.onCreate(s)
        binding = ActivityStudentListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val className = intent.getStringExtra("CLASS_NAME")!!

        // RecyclerView'ı ayarla
        binding.rvStudents.layoutManager = LinearLayoutManager(this)

        // Seçilen sınıfın öğrencilerini al
        val students = studentsByClass[className] ?: emptyList()

        // Ödev verilerini çek
        db.collection("odev_kontroller")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val classDoc = querySnapshot.documents.find { doc ->
                    doc.getString("sinif") == className
                }

                if (classDoc != null) {
                    val classId = classDoc.id
                    val odevAdi = classDoc.getString("odev_adi") ?: "Bilinmiyor"

                    students.forEach { student ->
                        // Ödev kontrolü
                        db.collection("odev_kontroller")
                            .document(classId)
                            .collection("ogrenciler")
                            .document(student.name)
                            .get()
                            .addOnSuccessListener { doc ->
                                if (doc.exists()) {
                                    student.odev = doc.getBoolean("odev_done") ?: false
                                    student.odevAdi = odevAdi
                                    updateAdapter(students)
                                }
                            }

                        // Kıyafet kontrolü
                        db.collection("kiyafetKontrol")
                            .document(className)
                            .collection("ogrenciler")
                            .document(student.name)
                            .get()
                            .addOnSuccessListener { doc ->
                                if (doc.exists()) {
                                    student.kiyafet = doc.getBoolean("isKiyafetDone") ?: false
                                    updateAdapter(students)
                                }
                            }

                        // Performans notu
                        db.collection("performansKontrol")
                            .document(className)
                            .collection("ogrenciler")
                            .document(student.name)
                            .get()
                            .addOnSuccessListener { doc ->
                                if (doc.exists()) {
                                    student.performans = doc.getLong("performansNot")?.toInt() ?: 0
                                    updateAdapter(students)
                                }
                            }

                        // Sınav verilerini çek
                        db.collection("uygulamaSinavlari")
                            .document(className)
                            .get()
                            .addOnSuccessListener { sinavDoc ->
                                val sinavAdi = sinavDoc.getString("sinav_adi") ?: "Bilinmiyor"
                                
                                // Öğrenci sınav notunu çek
                                db.collection("uygulamaSinavlari")
                                    .document(className)
                                    .collection("ogrenciler")
                                    .document(student.name)
                                    .get()
                                    .addOnSuccessListener { doc ->
                                        if (doc.exists()) {
                                            student.sinav = doc.getLong("grade")?.toInt() ?: 0
                                            student.sinavAdi = sinavAdi
                                            updateAdapter(students)
                                        }
                                    }
                            }
                    }
                }
            }

        // İlk başta boş durumları göster
        updateAdapter(students)
    }

    private fun updateAdapter(students: List<StudentResult>) {
        binding.rvStudents.adapter = StudentAdapter(students)
    }
}
