package com.example.newsapp.viewmodel

import android.app.Application
import android.content.SharedPreferences
import androidx.annotation.NonNull
import androidx.lifecycle.*
import com.example.newsapp.NewsApp
import com.example.newsapp.api.NewsApi
import com.example.newsapp.model.NewsResponse
import com.example.newsapp.model.datasource.NewsDataSource
import com.example.newsapp.utils.NetworkState
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainViewModel(@NonNull application: Application)  : AndroidViewModel(application) {

    private var searchResult: LiveData<NewsResponse>
    private var searchKey:MutableLiveData<String> = MutableLiveData()
    private  var liveDataList: MutableLiveData<NewsResponse> = MutableLiveData()
    private val compositeDisposable = CompositeDisposable()
    private  var networkState:MutableLiveData<NetworkState> = MutableLiveData()


    @Inject
    lateinit var mService:NewsApi

    @Inject
    lateinit var mSharedPreferences: SharedPreferences

    init {
        val country:String? = mSharedPreferences.getString("country","")
        val category:String? = mSharedPreferences.getString("category","")
        (application as NewsApp).getAppComponent().inject(this)
        searchResult = Transformations.switchMap(searchKey) {input ->
            makeApiCall(country!!,category!!,input)
        }
    }
    fun getRecyclerListObserver(): LiveData<NewsResponse> {
        return searchResult;
    }

    private fun makeApiCall(country:String, category:String, searchKey:String = ""): MutableLiveData<NewsResponse> {
        networkState.postValue(NetworkState.LOADING)
        compositeDisposable.add(
            mService.getArticles(country,category,searchKey).subscribeOn(Schedulers.io())
                .subscribe(
                    {
                    networkState.postValue(NetworkState.LOADED)
                        liveDataList.postValue(it)
                    },{
                      networkState.postValue(NetworkState.error(it.message))
                    }
                )

            )
        return liveDataList
    }

    fun setFilter(filter:String) {
        searchKey.value = filter
    }
    fun getNetworkState(): LiveData<NetworkState>{
        return networkState
    }
    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}