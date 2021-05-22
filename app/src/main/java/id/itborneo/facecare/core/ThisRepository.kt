package id.itborneo.facecare.core

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import id.itborneo.facecare.core.model.UserIdentifiedModel
import id.itborneo.facecare.core.model.UserInfoModel
import id.itborneo.facecare.core.networks.FaceCaseFirebase
import id.itborneo.facecare.utils.Resource

class ThisRepository {

    private val TAG = "ThisRepository"
    private val firebase = FaceCaseFirebase

    fun getSingleIdentifyUser(userId: String): LiveData<Resource<UserIdentifiedModel>> {

        val result = MutableLiveData<Resource<UserIdentifiedModel>>()
        result.postValue(Resource.loading(null))

        val identifyUserRef = firebase.getIdentifyUser(userId)

        identifyUserRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val data = dataSnapshot.getValue(UserIdentifiedModel::class.java)
                Log.d(TAG, "getSingleIdentifyUser identifyUserRef $userId and $data")

                if (data != null) {
                    Log.d(TAG, "getSingleIdentifyUser $data")

                    result.postValue(Resource.success(data))
                } else {
                    result.postValue(Resource.error(null, "null data"))
                }

            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
                result.postValue(Resource.error(null, "error ${error.message}"))

            }
        })
        return result
    }


    fun getSingleUserInfo(userId: String): LiveData<Resource<UserInfoModel>> {

        val result = MutableLiveData<Resource<UserInfoModel>>()
        result.postValue(Resource.loading(null))

        val userRef = firebase.getUser(userId)

        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val data = dataSnapshot.getValue(UserInfoModel::class.java)
                if (data != null) {
                    result.postValue(Resource.success(data))
                } else {
                    result.postValue(Resource.error(null, "null data"))
                }

            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
                result.postValue(Resource.error(null, "error ${error.message}"))

            }
        })
        return result
    }
}