package gomushin.backend.member.util

object CoupleCodeGeneratorUtil {
    fun generateCoupleCode(): String {
        val characters = ('A'..'Z') + ('0'..'9')
        return (1..6)
            .map { characters.random() }
            .joinToString("")
    }
}
