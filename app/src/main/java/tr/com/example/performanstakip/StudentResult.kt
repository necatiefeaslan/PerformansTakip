package tr.com.example.performanstakip

data class StudentResult(
    val name: String,
    val number: Int,
    var odev: Boolean = false,
    var odevAdi: String = "",
    var sinavAdi: String = "",
    var kiyafet: Boolean = false,
    var performans: Int = 0,
    var sinav: Int = 0
)