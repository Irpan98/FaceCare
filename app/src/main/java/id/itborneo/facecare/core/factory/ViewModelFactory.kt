package id.itborneo.ugithub.core.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import id.itborneo.facecare.home.HomeViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
//    private val repository: MainRepository,
    private val any: Any? = null,
    private val any2: Any? = null,

    ) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java))
            return HomeViewModel(
//                repository,
                any as String
            ) as T
//        if (modelClass.isAssignableFrom(FavoriteViewModel::class.java))
//            return FavoriteViewModel(repository) as T
//        if (modelClass.isAssignableFrom(HomeViewModel::class.java))
//            return HomeViewModel(repository) as T
//        if (modelClass.isAssignableFrom(ListInDetailViewModel::class.java))
//            return ListInDetailViewModel(repository, any as String, any2 as String) as T
        throw IllegalArgumentException()
    }

}