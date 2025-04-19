package tr.com.example.performanstakip

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import tr.com.example.performanstakip.databinding.ActivityDevamsizlikKontrolBinding

class DevamsizlikKontrolActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDevamsizlikKontrolBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDevamsizlikKontrolBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val className = intent.getStringExtra("CLASS_NAME") ?: ""
        val studentName = intent.getStringExtra("STUDENT_NAME") ?: "Bilinmeyen Öğrenci"

        binding.btnSaveDevamsizlik.setOnClickListener {
            val devamsizlikDurumu = binding.etDevamsizlikDurum.text.toString().trim()

            if (devamsizlikDurumu.isNotEmpty()) {
                val db = FirebaseFirestore.getInstance()

                val data = mapOf(
                    "devamsizlik" to devamsizlikDurumu
                )

                db.collection("kontroller")
                    .document(className)
                    .collection("ogrenciler")
                    .document(studentName)
                    .set(data, com.google.firebase.firestore.SetOptions.merge())
                    .addOnSuccessListener {
                        Toast.makeText(this, "Kaydedildi ✅", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Hata: ${it.message}", Toast.LENGTH_LONG).show()
                    }
            } else {
                Toast.makeText(this, "Lütfen boş bırakmayın", Toast.LENGTH_SHORT).show()
            }
        }
        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}
