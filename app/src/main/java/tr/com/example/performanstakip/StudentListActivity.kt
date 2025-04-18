package tr.com.example.performanstakip

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import tr.com.example.performanstakip.databinding.ActivityStudentListBinding

class StudentListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStudentListBinding
    private lateinit var studentAdapter: StudentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val className = intent.getStringExtra("CLASS_NAME") ?: ""
        Toast.makeText(this, "$className sınıfı seçildi", Toast.LENGTH_SHORT).show()

        // Öğrenci verileri: (ad, numara, not, devamsızlık, açıklama)
        val studentList = when (className) {
            "9-A" -> listOf(
                Student("Ahmet", 901, ),
                Student("Mehmet", 902, ),
                Student("Ayşe", 903, )
            )
            "9-B" -> listOf(
                Student("Ali", 904, ),
                Student("Veli", 905, ),
                Student("Ayşe", 906,)
            )
            "9-C" -> listOf(
                Student("Fatma", 907, ),
                Student("Mehmet", 908,),
                Student("Ayşe", 909,)
            )
            "10-A" -> listOf(
                Student("Kadir", 910, ),
                Student("Zeynep", 911, ),
                Student("Emre", 912,)
            )
            "10-B" -> listOf(
                Student("Can", 913,),
                Student("Ayla", 914,),
                Student("Fatma", 915, )
            )
            "10-C" -> listOf(
                Student("İsmail", 916,),
                Student("Meryem", 917,),
                Student("Seda", 918, )
            )
            "11-A" -> listOf(
                Student("Murat", 919, ),
                Student("Zeynep", 920, ),
                Student("Emre", 921, )
            )
            "11-B" -> listOf(
                Student("Can", 922,),
                Student("Ayla", 923, ),
                Student("Fatma", 924, )
            )
            "11-C" -> listOf(
                Student("İsmail", 925, ),
                Student("Meryem", 926, ),
                Student("Seda", 927,)
            )
            "12-A" -> listOf(
                Student("Murat", 928,),
                Student("Zeynep", 929, ),
                Student("Emre", 930, )
            )
            "12-B" -> listOf(
                Student("Can", 931, ),
                Student("Ayla", 932, ),
                Student("Fatma", 933,)
            )
            "12-C" -> listOf(
                Student("İsmail", 934,),
                Student("Meryem", 935,),
                Student("Seda", 936, )
            )

            else -> listOf(
                Student("Deneme", 9999,)
            )
        }

        // RecyclerView setup
        binding.rvStudentList.layoutManager = LinearLayoutManager(this)
        studentAdapter = StudentAdapter(studentList) { selectedStudent ->
            // Öğrenciye tıklandığında detay ekranına git
            val intent = Intent(this, StudentDetailActivity::class.java).apply {
                putExtra("STUDENT_NAME", selectedStudent.name)
                putExtra("STUDENT_NUMBER", selectedStudent.number)
                putExtra("CLASS_NAME", className)

            }
            startActivity(intent)
        }
        binding.rvStudentList.adapter = studentAdapter
    }
}