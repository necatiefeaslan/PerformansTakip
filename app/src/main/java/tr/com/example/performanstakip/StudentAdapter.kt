package tr.com.example.performanstakip

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tr.com.example.performanstakip.databinding.ItemStudentBinding

class StudentAdapter(
    private val students: List<Student>,
    private val onSwitchChanged: (Student, Boolean) -> Unit
) : RecyclerView.Adapter<StudentAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // ItemStudentBinding sınıfını inflate ediyoruz
        val binding = ItemStudentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val student = students[position]
        holder.bind(student)
    }

    inner class ViewHolder(private val binding: ItemStudentBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(student: Student) {
            binding.studentName.text = student.name
            binding.studentNumber.text = student.number.toString()

            // Switch butonunun durumunu ayarlıyoruz
            binding.switchOdev.isChecked = student.isOdevDone

            // Switch butonunun durumunun değişmesi
            binding.switchOdev.setOnCheckedChangeListener { _, isChecked ->
                onSwitchChanged(student, isChecked)
            }
        }
    }

    override fun getItemCount(): Int = students.size
}