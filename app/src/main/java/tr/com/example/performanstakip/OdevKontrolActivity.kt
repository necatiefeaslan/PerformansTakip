package tr.com.example.performanstakip

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import tr.com.example.performanstakip.databinding.ActivityOdevKontrolBinding

class OdevKontrolActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOdevKontrolBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOdevKontrolBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val className = intent.getStringExtra("CLASS_NAME") ?: ""
        val studentName = intent.getStringExtra("STUDENT_NAME") ?: "Bilinmeyen Öğrenci"

        binding.btnKaydet.setOnClickListener {
            val odevDurumu = binding.etOdevDurum.text.toString().trim()

            if (odevDurumu.isNotEmpty()) {
                val db = FirebaseFirestore.getInstance()

                val data = mapOf(
                    "odev" to odevDurumu
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
