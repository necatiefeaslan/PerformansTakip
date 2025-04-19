package tr.com.example.performanstakip

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import tr.com.example.performanstakip.databinding.ActivityKayitBinding

class KayitActivity : AppCompatActivity() {

    private lateinit var binding: ActivityKayitBinding
    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKayitBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        // Geri butonu
        binding.btnBack.setOnClickListener {
            finish()
        }

        // Kayıt olma işlemi
        binding.btnRegister.setOnClickListener {
            val eposta = binding.etEmail.text.toString().trim()
            val sifre = binding.etPassword.text.toString().trim()
            val ad = binding.etFirstName.text.toString().trim()
            val soyad = binding.etLastName.text.toString().trim()

            if (eposta.isEmpty() || sifre.isEmpty() || ad.isEmpty() || soyad.isEmpty()) {
                Toast.makeText(this, "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(eposta, sifre)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val uid = auth.currentUser?.uid ?: ""
                        val userMap = hashMapOf(
                            "ad" to ad,
                            "soyad" to soyad,
                            "email" to eposta,

                        )

                        db.collection("kullanicilar")
                            .document(uid)
                            .set(userMap)
                            .addOnCompleteListener { dbTask ->
                                if (dbTask.isSuccessful) {
                                    // ✅ Alanları temizle
                                    binding.etEmail.setText("")
                                    binding.etPassword.setText("")
                                    binding.etFirstName.setText("")
                                    binding.etLastName.setText("")

                                    Toast.makeText(this, "Kayıt başarılı. Lütfen giriş yapınız.", Toast.LENGTH_LONG).show()

                                    // Otomatik giriş yaptırmadan çıkış yap
                                    auth.signOut()

                                    // MainActivity'e yönlendir (giriş ekranı)
                                    startActivity(Intent(this, MainActivity::class.java))
                                    finish()
                                } else {
                                    Toast.makeText(this, "Firestore'a kaydedilemedi", Toast.LENGTH_LONG).show()
                                }
                            }
                    } else {
                        Toast.makeText(this, "Kayıt başarısız: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }
}
