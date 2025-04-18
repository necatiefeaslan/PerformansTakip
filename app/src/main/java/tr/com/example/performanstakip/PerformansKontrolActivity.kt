package tr.com.example.performanstakip

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import tr.com.example.performanstakip.databinding.ActivityPerformansKontrolBinding

class PerformansKontrolActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPerformansKontrolBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPerformansKontrolBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val className = intent.getStringExtra("CLASS_NAME") ?: "Bilinmeyen Sınıf"
        val studentName = intent.getStringExtra("STUDENT_NAME") ?: "Bilinmeyen Öğrenci"

        binding.btnSavePerformans.setOnClickListener {
            val performansDurumu = binding.etPerformans.text.toString().trim()

            if (performansDurumu.isNotEmpty()) {
                val db = FirebaseFirestore.getInstance()

                val data = mapOf(
                    "performans" to performansDurumu
                )

                db.collection("kontroller")
                    .document(className)
                    .collection("ogrenciler")
                    .document(studentName)
                    .set(data, com.google.firebase.firestore.SetOptions.merge()) // var olan verilere ekler

                    .addOnSuccessListener {
                        Toast.makeText(this, "Firestore'a kaydedildi ✅", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Hata: ${it.message}", Toast.LENGTH_LONG).show()
                    }
            } else {
                Toast.makeText(this, "Lütfen boş bırakmayın", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
