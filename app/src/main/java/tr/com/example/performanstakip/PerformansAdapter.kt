package tr.com.example.performanstakip

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tr.com.example.performanstakip.databinding.ItemStudentPerformansBinding

class PerformansAdapter(
    private val students: List<Student>,
    private val onPerformansChanged: (Student, Int) -> Unit
) : RecyclerView.Adapter<PerformansAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStudentPerformansBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val student = students[position]
        holder.bind(student)
    }

    inner class ViewHolder(private val binding: ItemStudentPerformansBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(student: Student) {
            binding.studentName.text = student.name
            binding.studentNumber.text = "No: ${student.number}"

            // Performans notu EditText
            binding.editTextPerformans.setText(student.performansNot?.toString() ?: "")
            binding.editTextPerformans.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    val performansNot = s.toString().toIntOrNull() ?: 0
                    if (performansNot in 0..100) {
                        onPerformansChanged(student, performansNot)
                    }
                }
            })
        }
    }

    override fun getItemCount(): Int = students.size
} 