package tr.com.example.performanstakip

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tr.com.example.performanstakip.databinding.ItemListBinding

class ListAdapter (
    private val items: List<SchoolClass>,
    private val onClick: (String) -> Unit
) : RecyclerView.Adapter<ListAdapter.VH>() {
    inner class VH(val b: ItemListBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(c: SchoolClass) {
            b.tvClassName.text = c.name
            b.root.setOnClickListener { onClick(c.name) }
        }
    }
    override fun onCreateViewHolder(p: ViewGroup, v: Int) =
        VH(ItemListBinding.inflate(LayoutInflater.from(p.context), p, false))
    override fun onBindViewHolder(h: VH, i: Int) = h.bind(items[i])
    override fun getItemCount() = items.size
}