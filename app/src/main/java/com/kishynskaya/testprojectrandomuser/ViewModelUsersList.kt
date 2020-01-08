package com.kishynskaya.testprojectrandomuser

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import io.reactivex.disposables.CompositeDisposable
import java.lang.Error
import java.util.concurrent.Executor

class ViewModelUsersList : ViewModel() {

    var userList: MutableLiveData<PagedList<Result>>? = null
    var error: MutableLiveData<Error>? = null

    private val compositeDisposable = CompositeDisposable()

    private val pageSize = 20

    init {
        val dataSource = UsersDataSource(compositeDisposable)
        error = dataSource.error
        val config: PagedList.Config = PagedList.Config.Builder()
            .setPageSize(pageSize)
            .setEnablePlaceholders(false)
            .build()
        val pagedList: PagedList<Result> =
            PagedList.Builder(dataSource, config).setNotifyExecutor(MainThreadExecutor())
                .setFetchExecutor(MainThreadExecutor()).build()
        userList = MutableLiveData()
        userList!!.postValue(pagedList)
    }

    class MainThreadExecutor : Executor {
        private val mHandler = Handler(Looper.getMainLooper())
        override fun execute(command: Runnable?) {
            mHandler.post(command)
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}