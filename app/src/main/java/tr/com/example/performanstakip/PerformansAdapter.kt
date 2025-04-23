package tr.com.example.performanstakip

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tr.com.example.performanstakip.databinding.ItemStudentPerformansBinding

class PerformansAdapter(
    private val students: List<Student>,
    private val onGradeChanged: (Student, Float?) -> Unit
) : RecyclerView.Adapter<PerformansAdapter.PerformansViewHolder>() {

    inner class PerformansViewHolder(private val binding: ItemStudentPerformansBinding) : RecyclerView.ViewHolder(binding.root) {
        private var textWatcher: TextWatcher? = null

        fun bind(student: Student) {
            binding.studentName.text = student.name
            binding.studentNumber.text = student.number.toString()
            
            // Remove previous TextWatcher if exists
            textWatcher?.let { binding.editTextPerformans.removeTextChangedListener(it) }
            
            // Set current grade if exists
            binding.editTextPerformans.setText(student.performansNot?.toInt()?.toString() ?: "")
            
            // Add new TextWatcher
            textWatcher = object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    val gradeText = s.toString()
                    if (gradeText.isEmpty()) {
                        onGradeChanged(student, null)
                        return
                    }
                    
                    val grade = gradeText.toFloatOrNull()
                    if (grade != null) {
                        if (grade < 0 || grade > 100) {
                            binding.editTextPerformans.error = "Not 0-100 arasında olmalıdır"
                        } else {
                            binding.editTextPerformans.error = null
                            onGradeChanged(student, grade)
                        }
                    } else {
                        binding.editTextPerformans.error = "Geçerli bir not giriniz"
                    }
                }
            }
            
            binding.editTextPerformans.addTextChangedListener(textWatcher)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PerformansViewHolder {
        val binding = ItemStudentPerformansBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PerformansViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PerformansViewHolder, position: Int) {
        holder.bind(students[position])
    }

    override fun getItemCount() = students.size
} 