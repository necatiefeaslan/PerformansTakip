package tr.com.example.performanstakip

data class Student(
    val name: String,
    val number: Int,
    var isOdevDone: Boolean = false,
    var isKiyafetDone: Boolean = false,
    var isDevamsizlikDone: Boolean = false,
    var performansNot: String? = null // Burada performansNot'u kullanıyoruz
)


