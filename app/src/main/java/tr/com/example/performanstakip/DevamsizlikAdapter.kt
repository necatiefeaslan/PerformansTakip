package tr.com.example.performanstakip

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tr.com.example.performanstakip.databinding.ItemStudentDevamsizlikBinding

class DevamsizlikAdapter(
    private val students: List<Student>,
    private val onDevamsizlikChanged: (Student, Boolean) -> Unit
) : RecyclerView.Adapter<DevamsizlikAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStudentDevamsizlikBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val student = students[position]
        holder.bind(student)
    }

    inner class ViewHolder(private val binding: ItemStudentDevamsizlikBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(student: Student) {
            binding.studentName.text = student.name
            binding.studentNumber.text = "No: ${student.number}"

            // Devamsızlık Switch
            binding.switchDevamsizlik.isChecked = student.isDevamsizlikDone
            binding.switchDevamsizlik.setOnCheckedChangeListener { _, isChecked ->
                onDevamsizlikChanged(student, isChecked)
            }
        }
    }

    override fun getItemCount(): Int = students.size
} 