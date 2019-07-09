package com.example.myapplication

import com.example.myapplication.LoadType.*
import io.reactivex.*
import io.reactivex.subjects.BehaviorSubject

class Repo {

    private val cache = mutableListOf<String>()
    val subject: BehaviorSubject<LoadType> = BehaviorSubject.create()
    val api: API = ApiImpl()


    private fun getItems(): Single<List<String>>{
        return api.getItems().doOnSuccess {
                    cache.clear()
                    cache.addAll(it)
               }
    }

    private fun updateAndGetItems(): Single<List<String>>{
        return api.getItemsFrom(cache.size)
            .onErrorReturn { cache }
            .map {
                cache.addAll(it)
                cache
            }
    }

    fun loadMore() = subject.onNext(LOAD_MORE)
    fun update() = subject.onNext(UPDATE)



    fun fetchItems(): Flowable<List<String>>{
        return subject.switchMap { loadType ->
            return@switchMap when(loadType){
                 UPDATE -> getItems().toObservable()
                 LOAD_MORE -> updateAndGetItems().toObservable()
            }
        }.toFlowable(BackpressureStrategy.LATEST)
    }
}

enum class LoadType{
    UPDATE,
    LOAD_MORE
}