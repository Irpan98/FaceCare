package id.itborneo.facecare.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import id.itborneo.facecare.core.ThisRepository
import id.itborneo.facecare.core.model.UserIdentifiedModel
import id.itborneo.facecare.utils.enums.HomeEnum

class HomeViewModel(private var idUser: String) : ViewModel() {
    private val repo = ThisRepository()

    private val loadTrigger = MutableLiveData(Unit) //for refresh

    var homeState = MutableLiveData(HomeEnum.NOT_REGISTERED)


    fun refreshUserId(userId: String) {
        loadTrigger.value = Unit
        idUser = userId
    }

    var userIdentifiedModel = MutableLiveData<UserIdentifiedModel>()


    private var identifyUser = loadTrigger.switchMap {
        repo.getSingleIdentifyUser(idUser)
    }
    private val userInfo = loadTrigger.switchMap {
        repo.getSingleUserInfo(idUser)
    }


    fun getIdentifyUser() = identifyUser
    fun getUserInfo() = userInfo
    fun getArticle() = repo.getArticle()


}