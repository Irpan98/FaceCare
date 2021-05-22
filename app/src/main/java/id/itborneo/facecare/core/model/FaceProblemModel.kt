package id.itborneo.facecare.core.model

data class FaceProblemModel(
    val nama: String = "",
    val penjelasan: String = "",
    val solusi_herbal_1: MutableList<NaturalIngredientModel> = mutableListOf()
)
