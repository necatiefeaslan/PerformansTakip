package tr.com.example.performanstakip

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tr.com.example.performanstakip.databinding.ItemStudentKiyafetBinding

class KiyafetAdapter(
    private val students: List<Student>,
    private val onKiyafetChanged: (Student, Boolean) -> Unit
) : RecyclerView.Adapter<KiyafetAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStudentKiyafetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val student = students[position]
        holder.bind(student)
    }

    inner class ViewHolder(private val binding: ItemStudentKiyafetBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(student: Student) {
            binding.studentName.text = student.name
            binding.studentNumber.text = "No: ${student.number}"

            // Kıyafet Switch
            binding.switchKiyafet.isChecked = student.isKiyafetDone
            binding.switchKiyafet.setOnCheckedChangeListener { _, isChecked ->
                onKiyafetChanged(student, isChecked)
            }
        }
    }

    override fun getItemCount(): Int = students.size
} 