package gomushin.backend.couple.dto.response

data class DdayResponse (
        val sinceLove : String?,
        val sinceMilitaryStart : String?,
        val militaryEndLeft : String?
){
    companion object{
        fun of(
            sinceLove: Int?,
            sinceMilitaryStart: Int?,
            militaryEndLeft: Int?
        ): DdayResponse {
            fun format(day: Int?): String? =
                day?.let { if (it > 0) "+$it" else if (it == 0) "-DAY" else it.toString() }

            return DdayResponse(
                sinceLove = format(sinceLove),
                sinceMilitaryStart = format(sinceMilitaryStart),
                militaryEndLeft = format(militaryEndLeft)
            )
        }
    }
}