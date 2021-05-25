package id.itborneo.facecare.core.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import id.itborneo.facecare.home.HomeViewModel
import id.itborneo.facecare.result.ResultViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
    private val any: Any? = null,
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java))
            return HomeViewModel(
                any as String
            ) as T
        else if (modelClass.isAssignableFrom(ResultViewModel::class.java))
            return ResultViewModel(
                any as String
            ) as T

        throw IllegalArgumentException()
    }

}