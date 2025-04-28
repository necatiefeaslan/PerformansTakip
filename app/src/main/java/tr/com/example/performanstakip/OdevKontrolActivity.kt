package tr.com.example.performanstakip

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import tr.com.example.performanstakip.databinding.ActivityOdevKontrolBinding
import java.text.SimpleDateFormat
import java.util.*

class OdevKontrolActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOdevKontrolBinding
    private lateinit var odevAdapter: OdevAdapter
    private lateinit var studentsList: List<Student>
    private var saveCount = 0
    private val calendar = Calendar.getInstance()
    private val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOdevKontrolBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Geri tuşuna basıldığında aktiviteyi kapat
        binding.btnBack.setOnClickListener {
            finish()
        }

        // Class adı alınıyor
        val className = intent.getStringExtra("CLASS_NAME") ?: ""

        // Bugünün tarihini ayarla
        binding.etTarih.setText(dateFormat.format(calendar.time))

        // Tarih seçici tıklama
        binding.etTarih.setOnClickListener {
            showDatePicker()
        }

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
            val odevAdi = binding.etOdevAdi.text.toString().trim()
            if (odevAdi.isEmpty()) {
                Toast.makeText(this, "Lütfen ödev adını giriniz", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            saveStudentControls(className, studentsList, odevAdi, binding.etTarih.text.toString())
        }
    }

    private fun showDatePicker() {
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                binding.etTarih.setText(dateFormat.format(calendar.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun getStudents(): List<Student> {
        // Öğrencilerin listesi Firestore'dan alınabilir
        return listOf(
            Student("Ahmet Yılmaz", 123),
            Student("Mehmet Kaya", 124),
            Student("Ayşe Demir", 125)
        )
    }

    private fun saveStudentControls(className: String, students: List<Student>, odevAdi: String, tarih: String) {
        val db = FirebaseFirestore.getInstance()
        saveCount = 0

        // Ödev kontrolünün ana dokümanını oluştur
        val odevKontrolData = hashMapOf(
            "odev_adi" to odevAdi,
            "tarih" to tarih,
            "sinif" to className
        )

        // Önce ödev kontrol dokümanını oluştur
        db.collection("odev_kontroller")
            .document(className)  // className'i ID olarak kullan
            .set(odevKontrolData)  // set() kullanarak dokümanı oluştur
            .addOnSuccessListener {
                // Sonra öğrenci kontrollerini kaydet
                students.forEach { student ->
                    val studentData = hashMapOf(
                        "name" to student.name,
                        "number" to student.number,
                        "odev_done" to student.isOdevDone
                    )

                    db.collection("odev_kontroller")
                        .document(className)
                        .collection("ogrenciler")
                        .document(student.name)
                        .set(studentData)
                        .addOnSuccessListener {
                            saveCount++
                            if (saveCount == students.size) {
                                Toast.makeText(this, "Ödev kontrolleri başarıyla kaydedildi", Toast.LENGTH_SHORT).show()
                                finish() // Aktiviteyi kapat
                            }
                        }
                        .addOnFailureListener { exception ->
                            Toast.makeText(this, "Hata: ${exception.message}", Toast.LENGTH_LONG).show()
                        }
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Hata: ${exception.message}", Toast.LENGTH_LONG).show()
            }
    }
}
