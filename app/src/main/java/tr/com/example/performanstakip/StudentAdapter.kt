package tr.com.example.performanstakip

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tr.com.example.performanstakip.databinding.ItemStudentBinding


class StudentAdapter(private val studentList: List<Student>, private val onItemClick: (Student) -> Unit) :
    RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    inner class StudentViewHolder(val binding: ItemStudentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(student: Student) {
            // Öğrenci bilgilerini bağlama
            binding.tvStudentName.text = "Öğrenci Adı: ${student.name} (Numarası: ${student.number})"

            // Öğrenciye tıklandığında detay sayfasına git
            binding.root.setOnClickListener {
                onItemClick(student)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val binding = ItemStudentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StudentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        holder.bind(studentList[position])
    }

    override fun getItemCount(): Int = studentList.size
}
