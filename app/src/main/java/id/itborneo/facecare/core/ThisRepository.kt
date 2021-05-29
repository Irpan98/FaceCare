package id.itborneo.facecare.core

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import id.itborneo.facecare.core.model.*
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


    fun getArticle(): LiveData<Resource<List<ArticleModel>>> {
        Log.d(TAG, "articleRef ")

        val result = MutableLiveData<Resource<List<ArticleModel>>>()
        result.postValue(Resource.loading(null))

        val articleRef = firebase.getArticle()


        articleRef.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {


                val articles = mutableListOf<ArticleModel>()
                Log.d(TAG, "articleRef $articles")

                dataSnapshot.children.forEachIndexed { index, snapshot ->
                    val getData = snapshot.getValue(ArticleModel::class.java)
                    if (getData != null) {
                        articles.add(getData)
                    }
                }
                result.postValue(Resource.success(articles))
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
                result.postValue(Resource.error(null, "error ${error.message}"))

            }
        })
        return result
    }

    fun getListFaceProblem(): LiveData<Resource<List<FaceProblemModel>>> {
        Log.d(TAG, "getListFaceProblem ")

        val result = MutableLiveData<Resource<List<FaceProblemModel>>>()
        result.postValue(Resource.loading(null))

        val articleRef = firebase.getIdentification()


        articleRef.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val data = mutableListOf<FaceProblemModel>()

                dataSnapshot.children.forEachIndexed { index, snapshot ->
                    val getData = snapshot.getValue(FaceProblemModel::class.java)
                    if (getData != null) {
                        data.add(getData)
                    }
                }
                Log.d(TAG, "getListFaceProblem $data")

                result.postValue(Resource.success(data))
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
                result.postValue(Resource.error(null, "error ${error.message}"))

            }
        })
        return result
    }

    fun getListChat(): LiveData<Resource<List<MessageModel>>> {
        Log.d(TAG, "getListFaceProblem ")

        val result = MutableLiveData<Resource<List<MessageModel>>>()
        result.postValue(Resource.loading(null))

        val ref = firebase.getChat()

        ref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val data = mutableListOf<MessageModel>()

                dataSnapshot.children.forEachIndexed { index, snapshot ->
                    val getData = snapshot.getValue(MessageModel::class.java)
                    if (getData != null) {
                        data.add(getData)

                    }
                }

                result.postValue(Resource.success(data))
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


