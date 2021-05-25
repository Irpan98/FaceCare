package id.itborneo.facecare.result

import androidx.lifecycle.ViewModel
import id.itborneo.facecare.core.ThisRepository

class ResultViewModel(faceProblem: String) : ViewModel() {

    private val repo = ThisRepository()

    fun getListFaceProblems() = repo.getListFaceProblem()

}