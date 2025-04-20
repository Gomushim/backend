package gomushin.backend.couple.dto.response

data class DdayResponse (
        val sinceLove : Int?,
        val sinceMilitaryStart : Int?,
        val militaryEndLeft : Int?
){
    companion object{
        fun of(sinceLove: Int,
               sinceMilitaryStart: Int,
               militaryEndLeft: Int) = DdayResponse (
                sinceLove,
                sinceMilitaryStart,
                militaryEndLeft
        )
    }
}