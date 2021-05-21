package id.itborneo.facecare.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.itborneo.facecare.model.UserIdentifiedModel
import id.itborneo.facecare.utils.KsPrefUser
import id.itborneo.facecare.utils.enums.HomeEnum

class HomeViewModel() : ViewModel() {

    var idUser: String = KsPrefUser.NOT_REGISTERED
    var homeState = MutableLiveData(HomeEnum.NOT_REGISTERED)
    val user = MutableLiveData<UserIdentifiedModel>()


}