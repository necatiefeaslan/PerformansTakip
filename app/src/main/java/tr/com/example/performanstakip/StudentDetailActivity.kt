package tr.com.example.performanstakip

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import tr.com.example.performanstakip.databinding.ActivityStudentDetailBinding

class StudentDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStudentDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // İntent'ten gelen sınıf ve öğrenci ismini al
        val className = intent.getStringExtra("CLASS_NAME") ?: ""


        // Ekrana öğrenci adını yaz
        binding.tvStudentName.text = className

        // Ödev kontrol sayfasına git
        binding.odevKontrol.setOnClickListener {
            val intent = Intent(this, OdevKontrolActivity::class.java)
            intent.putExtra("CLASS_NAME", className)

            startActivity(intent)
        }

        // Kıyafet kontrol sayfasına git
        binding.kiyafetKontrol.setOnClickListener {
            val intent = Intent(this, KiyafetKontrolActivity::class.java)
            intent.putExtra("CLASS_NAME", className)

            startActivity(intent)
        }

        // Performans kontrol sayfasına git
        binding.performans100Kontrol.setOnClickListener {
            val intent = Intent(this, PerformansKontrolActivity::class.java)
            intent.putExtra("CLASS_NAME", className)

            startActivity(intent)
        }

        // ✅ Devamsızlık kontrol sayfasına git
        binding.devamsizlikKontrol.setOnClickListener {
            val intent = Intent(this, DevamsizlikKontrolActivity::class.java)
            intent.putExtra("CLASS_NAME", className)

            startActivity(intent)
        }

        // Geri butonu
        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}
