package tr.com.example.performanstakip

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tr.com.example.performanstakip.databinding.ItemStudentUygulamaSinavBinding

class UygulamaSinavAdapter(
    private val students: List<Student>,
    private val onGradeChanged: (Student, Int) -> Unit,
    private val sinavAdi: String
) : RecyclerView.Adapter<UygulamaSinavAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemStudentUygulamaSinavBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(student: Student) {
            binding.studentNameTextView.text = student.name
            binding.studentNumberTextView.text = "No: ${student.number}"
            
            // Sınav adı ve notu göster
            binding.tvSinav.text = if (sinavAdi.isNotEmpty() && student.grade != null) {
                "$sinavAdi - ${student.grade}"
            } else {
                ""  // Boş durumda hiçbir şey gösterme
            }

            // Mevcut notu göster
            binding.gradeEditText.setText(student.grade?.toString() ?: "")

            // Önceki TextWatcher'ı kaldır
            binding.gradeEditText.removeTextChangedListener(binding.gradeEditText.tag as? TextWatcher)

            // Yeni TextWatcher oluştur
            val textWatcher = object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    val grade = s.toString().toIntOrNull()
                    if (grade != null && grade in 0..100) {
                        student.grade = grade
                        onGradeChanged(student, grade)
                        binding.gradeEditText.error = null
                    } else {
                        student.grade = null
                        if (s?.isNotEmpty() == true) {
                            binding.gradeEditText.error = "0-100 arası bir değer giriniz"
                        }
                    }
                }
            }

            // TextWatcher'ı kaydet ve ekle
            binding.gradeEditText.tag = textWatcher
            binding.gradeEditText.addTextChangedListener(textWatcher)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStudentUygulamaSinavBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(students[position])
    }

    override fun getItemCount() = students.size
}
