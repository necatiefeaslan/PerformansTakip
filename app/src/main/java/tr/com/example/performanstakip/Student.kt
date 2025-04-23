package tr.com.example.performanstakip

data class Student(
    var name: String = "",
    var number: Int = 0,
    var grade: Int? = null,
    var isOdevDone: Boolean = false,
    var isKiyafetDone: Boolean = false,
    var uygulamaSinavNotu: Int? = null,
    var performansNot: Float? = null,
)


