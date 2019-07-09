package com.example.myapplication

import io.reactivex.Single

interface API {

    fun getItems(): Single<List<String>>
    fun getItemsFrom(from:Int): Single<List<String>>
}