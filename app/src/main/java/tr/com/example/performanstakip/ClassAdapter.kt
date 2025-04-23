package tr.com.example.performanstakip

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tr.com.example.performanstakip.databinding.ItemClassBinding

class ClassAdapter(
    private val classList: List<String>,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<ClassAdapter.ClassViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassViewHolder {
        val binding = ItemClassBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ClassViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClassViewHolder, position: Int) {
        val className = classList[position]
        holder.binding.tvClassName.text = className
        holder.itemView.setOnClickListener {
            onItemClick(className)
        }
    }

    override fun getItemCount(): Int = classList.size

    class ClassViewHolder(val binding: ItemClassBinding) : RecyclerView.ViewHolder(binding.root)
}