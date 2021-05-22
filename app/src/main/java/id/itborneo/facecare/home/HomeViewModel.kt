package id.itborneo.facecare.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.itborneo.facecare.core.ThisRepository
import id.itborneo.facecare.core.model.UserIdentifiedModel
import id.itborneo.facecare.core.model.UserInfoModel
import id.itborneo.facecare.utils.Resource
import id.itborneo.facecare.utils.enums.HomeEnum

class HomeViewModel(idUser: String) : ViewModel() {

    var homeState = MutableLiveData(HomeEnum.NOT_REGISTERED)
    var user = MutableLiveData<UserInfoModel>()
    var userIdentifiedModel = MutableLiveData<UserIdentifiedModel>()

    val repo = ThisRepository()

    private val TAG = "HomeViewModel"
    private var identifyUser = repo.getSingleIdentifyUser(idUser)
    private val userInfo = MutableLiveData<Resource<UserInfoModel>>()


    fun setSingleIdentifyUser() {
//        Log.d(TAG, "setSingleIdentifyUser ")
//
//        identifyUser = Transformations.map(repo.getSingleIdentifyUser(idUser)) {
//            it
//        } as MutableLiveData<Resource<UserIdentifiedModel>>

    }


    fun getIdentifyUser() = identifyUser

}