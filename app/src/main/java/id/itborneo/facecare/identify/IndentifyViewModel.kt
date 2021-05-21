package id.itborneo.facecare.identify

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.itborneo.facecare.model.UserIdentifiedModel

class IndentifyViewModel() : ViewModel() {

    var userIndentified = MutableLiveData(UserIdentifiedModel())

}