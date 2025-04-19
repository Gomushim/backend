package gomushin.backend.couple.dto.response
data class CoupleGradeResponse(
        val grade : Int
){
    companion object{
        fun of(grade: Int) = CoupleGradeResponse(
                grade
        )
    }
}
