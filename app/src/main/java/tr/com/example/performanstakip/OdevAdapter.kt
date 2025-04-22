package tr.com.example.performanstakip

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tr.com.example.performanstakip.databinding.ItemStudentOdevBinding

class OdevAdapter(
    private val students: List<Student>,
    private val onOdevChanged: (Student, Boolean) -> Unit
) : RecyclerView.Adapter<OdevAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStudentOdevBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val student = students[position]
        holder.bind(student)
    }

    inner class ViewHolder(private val binding: ItemStudentOdevBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(student: Student) {
            binding.studentName.text = student.name
            binding.studentNumber.text = "No: ${student.number}"

            // Ödev Switch
            binding.switchOdev.isChecked = student.isOdevDone
            binding.switchOdev.setOnCheckedChangeListener { _, isChecked ->
                onOdevChanged(student, isChecked)
            }
        }
    }

    override fun getItemCount(): Int = students.size
} 