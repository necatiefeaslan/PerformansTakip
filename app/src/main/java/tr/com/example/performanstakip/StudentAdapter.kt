package tr.com.example.performanstakip

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tr.com.example.performanstakip.databinding.ItemStudentCardBinding

class StudentAdapter(private val list: List<StudentResult>) :
    RecyclerView.Adapter<StudentAdapter.VH>() {
    inner class VH(val b: ItemStudentCardBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(s: StudentResult) {
            b.tvName.text = s.name
            b.tvNumber.text = s.number.toString()
            b.tvOdev.text = "Ödev: ${s.odevAdi} - ${if(s.odev) "Yapıldı" else "Yapılmadı"}"
            b.tvKiyafet.text = "Kıyafet: ${if(s.kiyafet) "Uygun" else "Uygun Değil"}"
            b.tvPerformans.text = "Performans: ${s.performans}"
            b.tvSinav.text = "Uyg. Sınav: ${s.sinavAdi} - ${s.sinav}"
        }
    }
    override fun onCreateViewHolder(p: ViewGroup, v: Int) =
        VH(ItemStudentCardBinding.inflate(LayoutInflater.from(p.context), p, false))
    override fun onBindViewHolder(h: VH, i: Int) = h.bind(list[i])
    override fun getItemCount() = list.size
}