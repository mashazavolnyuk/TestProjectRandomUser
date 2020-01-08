package com.kishynskaya.testprojectrandomuser

import androidx.lifecycle.MutableLiveData
import androidx.paging.PositionalDataSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class UsersDataSource(compositeDisposable: CompositeDisposable?) :
    PositionalDataSource<Result>() {

    private var compositeDisposable: CompositeDisposable? = null
    var error: MutableLiveData<Error> = MutableLiveData()

    init {
        this.compositeDisposable = compositeDisposable
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Result>) {
        compositeDisposable!!.add(ServerAPI.netService.getUsers(
            params.startPosition,
            20
        ).observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe { data, throwable ->
                if (throwable != null) {
                    error.postValue(java.lang.Error(throwable.message))
                } else {
                    val listResult = data.results
                    callback.onResult(listResult)
                }
            }
        )
    }

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Result>) {
        compositeDisposable!!.add(ServerAPI.netService.getUsers(1, 20)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe { data, throwable ->
                if (throwable != null) {
                    error.postValue(java.lang.Error(throwable.message))
                } else {
                    val listResult = data.results
                    callback.onResult(listResult, 0)
                }
            }
        )
    }
}