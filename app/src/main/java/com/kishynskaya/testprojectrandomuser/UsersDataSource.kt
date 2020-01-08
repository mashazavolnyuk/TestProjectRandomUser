package com.kishynskaya.testprojectrandomuser

import androidx.lifecycle.MutableLiveData
import androidx.paging.PositionalDataSource
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers


class UsersDataSource(compositeDisposable: CompositeDisposable?) :
    PositionalDataSource<Result>() {

    private var compositeDisposable: CompositeDisposable? = null

    private var retryCompletable: Completable? = null

    var error: MutableLiveData<Error> = MutableLiveData()

    init {
        this.compositeDisposable = compositeDisposable
    }

    fun retry() {
        if (retryCompletable != null) {
            compositeDisposable!!.add(
                retryCompletable!!
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({}) { throwable: Throwable -> }
            )
        }
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
                    // keep a Completable for future retry
                    setRetry(Action { loadRange(params, callback) })
                } else {
                    val listResult = data.results
                    callback.onResult(listResult)
                    setRetry(null)
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
                    setRetry(Action { loadInitial(params, callback) })
                } else {
                    val listResult = data.results
                    callback.onResult(listResult, 0)
                    setRetry(null)
                }
            }
        )
    }

    private fun setRetry(action: Action?) {
        retryCompletable = if (action == null) {
            null
        } else {
            Completable.fromAction(action)
        }
    }
}