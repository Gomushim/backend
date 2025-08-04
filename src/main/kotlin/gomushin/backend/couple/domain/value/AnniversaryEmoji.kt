package gomushin.backend.couple.domain.value

enum class AnniversaryEmoji {
    HEART, CALENDAR, CAKE, TRAVEL;

    companion object {
        fun getByName(name: String?): AnniversaryEmoji? {
            return entries.find { it.name.equals(name, ignoreCase = true) }
        }
    }
}
