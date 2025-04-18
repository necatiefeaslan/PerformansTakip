package tr.com.example.performanstakip

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import tr.com.example.performanstakip.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()



        // Giriş butonu
        binding.btnLogin.setOnClickListener {
            val eposta = binding.etEmail.text.toString()
            val sifre = binding.etPassword.text.toString()

            if (eposta.isEmpty() || sifre.isEmpty()) {
                Toast.makeText(this, "Boş alanları doldurunuz", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(eposta, sifre)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Giriş Başarılı", Toast.LENGTH_LONG).show()
                        val intent = Intent(this, ClassListActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "Giriş Başarısız: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
        }

        // Kayıt ekranına geçiş
        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, KayitActivity::class.java))
        }
    }
}
