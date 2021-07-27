package com.capsule.healthytimes.core.di.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

/**
 * ViewModelProvider factory for using dagger with ViewModel with constructors.
 *
 * With dagger multibindings we create a “map” with
 *
 *      the key: for example the class type of ViewModel MyViewModel::class
 *      the value: the instance of ViewModel MyViewModel(repository, …)
 *
 * Dagger ( in compiling time) will create the map and we will provide them to ViewModelFactory as argument,
 * so when we will invoke the create() method it will able to pick the right instance from the map
 */
@Suppress("UNCHECKED_CAST")
class ViewModelProviderFactory @Inject constructor(private val viewModelsMap: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val creator = viewModelsMap[modelClass] ?: viewModelsMap.asIterable().firstOrNull {
            modelClass.isAssignableFrom(it.key)
        }?.value ?: throw IllegalArgumentException("unknown model class $modelClass")
        return try {
            creator.get() as T
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}
