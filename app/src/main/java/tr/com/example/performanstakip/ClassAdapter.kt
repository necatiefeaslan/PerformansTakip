package tr.com.example.performanstakip


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ClassAdapter(
    private val classList: List<String>,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<ClassAdapter.ClassViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return ClassViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClassViewHolder, position: Int) {
        val className = classList[position]
        holder.classNameText.text = className
        holder.itemView.setOnClickListener {
            onItemClick(className)
        }
    }

    override fun getItemCount(): Int = classList.size

    class ClassViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val classNameText: TextView = itemView.findViewById(android.R.id.text1)
    }
}

