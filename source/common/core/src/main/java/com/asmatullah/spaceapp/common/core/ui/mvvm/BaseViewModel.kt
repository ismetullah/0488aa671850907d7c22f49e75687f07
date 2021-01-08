package com.asmatullah.spaceapp.common.core.ui.mvvm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.asmatullah.spaceapp.common.core.R
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.net.UnknownHostException
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel : ViewModel(), CoroutineScope {
    val progressLoader = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()
    val errorResource = MutableLiveData<Int>()
    var errorListener: (() -> Unit)? = null

    protected val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        handleException(exception)
    }

    override val coroutineContext: CoroutineContext =
        SupervisorJob() + Dispatchers.IO + exceptionHandler

    private fun handleException(exception: Throwable) {
        progressLoader.postValue(false)
        try {
            if (exception is HttpException) {
                if (exception.code() == 404) {
                    errorListener?.invoke()
                    return
                }
                errorMessage.postValue(exception.message())
            } else if (exception is UnknownHostException) {
                errorResource.postValue(R.string.error_network)
            } else {
                errorResource.postValue(R.string.error_common)
            }
            exception.printStackTrace()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    protected fun handleRequest(
		scope: CoroutineScope = this,
		listener: suspend CoroutineScope.() -> Unit
	): Job {
        val job = scope.launch {
            progressLoader.postValue(true)
            try {
                listener.invoke(this)
            } catch (exception: CancellationException) {
                exception.printStackTrace()
            } catch (exception: Throwable) {
                handleException(exception)
            }
        }
        job.invokeOnCompletion {
            progressLoader.postValue(false)
        }
        return job
    }

    protected fun handleRequest(
		job: Job, listener: suspend CoroutineScope.() -> Unit
	): Job {
        val response = launch(job) {
            progressLoader.postValue(true)
            try {
                listener.invoke(this)
            } catch (exception: CancellationException) {
                exception.printStackTrace()
            } catch (exception: Throwable) {
                handleException(exception)
            }
        }
        response.invokeOnCompletion {
            progressLoader.postValue(false)
        }
        return response
    }

    override fun onCleared() {
        super.onCleared()
        cancel()
    }
}