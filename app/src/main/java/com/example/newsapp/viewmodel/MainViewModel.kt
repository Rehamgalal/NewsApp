package com.example.newsapp.viewmodel

import android.app.Application
import android.content.SharedPreferences
import androidx.annotation.NonNull
import androidx.lifecycle.*
import com.example.newsapp.NewsApp
import com.example.newsapp.api.NewsApi
import com.example.newsapp.db.ArticleEntity
import com.example.newsapp.db.ArticlesDatabase
import com.example.newsapp.model.toArticleEntity
import com.example.newsapp.utils.NetworkState
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainViewModel(@NonNull application: Application) : AndroidViewModel(application) {

    private var searchResult: LiveData<List<ArticleEntity>>
    private var searchKey: MutableLiveData<String> = MutableLiveData()
    private var liveDataList: MutableLiveData<List<ArticleEntity>> = MutableLiveData()
    private val compositeDisposable = CompositeDisposable()
    private var networkState: MutableLiveData<NetworkState> = MutableLiveData()


    @Inject
    lateinit var mService: NewsApi

    @Inject
    lateinit var mSharedPreferences: SharedPreferences

    @Inject
    lateinit var dataBase: ArticlesDatabase

    init {
        (application as NewsApp).getAppComponent().inject(this)
        val country: String? = mSharedPreferences.getString("country", "")
        val category1: String? = mSharedPreferences.getString("category1", "")
        val category2: String? = mSharedPreferences.getString("category2", "")
        val category3: String? = mSharedPreferences.getString("category3", "")

        searchResult = Transformations.switchMap(searchKey) { input ->
            this.makeApiCall(country!!, category1!!, input)
            this.makeApiCall(country, category2!!, input)
            this.makeApiCall(country, category3!!, input)
        }
    }

    fun getRecyclerListObserver(): LiveData<List<ArticleEntity>> {
        return searchResult
    }

    private fun makeApiCall(
            country: String,
            category: String,
            searchKey: String = ""
    ): MutableLiveData<List<ArticleEntity>> {
        this.networkState.postValue(NetworkState.LOADING)
        compositeDisposable.add(
                mService.getArticles(country, category, searchKey).map { newsResponse ->
                    newsResponse.articles.map { it.toArticleEntity() }
                }.doOnSuccess {
                    dataBase.articlesDao().insert(it)
                }.subscribeOn(Schedulers.io())
                        .subscribe(
                                {
                                    liveDataList.postValue(dataBase.articlesDao().allArticlesEntities())
                                    networkState.postValue(NetworkState.LOADED)
                                }, {
                            networkState.postValue(NetworkState.error(it.message))
                            liveDataList.postValue(dataBase.articlesDao().allArticlesEntities())
                        }
                        )

        )
        return liveDataList
    }

    fun searchArticle(word:String) : LiveData<List<ArticleEntity>> {
        return dataBase.articlesDao().searchArticles(word)
    }

    fun setFilter(filter: String?) {
        if (filter == null) searchKey.value = "" else searchKey.value = filter
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return networkState
    }

    fun updateItem(articleEntity: ArticleEntity) {
        Completable.fromRunnable {
            dataBase.articlesDao().insert(articleEntity)
        }.subscribeOn(Schedulers.io()).subscribe()

    }

    fun getLikedArticles(): LiveData<List<ArticleEntity>> {
        return dataBase.articlesDao().favArticlesEntities()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}