package com.example.newsapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.newsapp.api.NewsApi
import com.example.newsapp.model.NewsResponse
import com.example.newsapp.utils.NetworkState
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainViewModel @Inject constructor(val mService: NewsApi) : ViewModel() {

    private var searchResult: LiveData<NewsResponse>
    private var searchKey:MutableLiveData<String> = MutableLiveData()
    private  var liveDataList: MutableLiveData<NewsResponse> = MutableLiveData()
    private val compositeDisposable = CompositeDisposable()
    private  var networkState:MutableLiveData<NetworkState> = MutableLiveData()

    init {
        searchResult = Transformations.switchMap(searchKey) {input ->
            makeApiCall("eg","entertainment",input)
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