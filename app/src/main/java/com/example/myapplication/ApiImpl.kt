package com.example.myapplication

import io.reactivex.Single

class ApiImpl: API {


    private fun getRange(from: Int = 0): List<String> {
        return listOf((from..from+10)).map {
            "$it - Value"
        }.toList()
    }

    override fun getItems(): Single<List<String>> {
        return Single.just(getRange(0))
    }

    override fun getItemsFrom(from: Int): Single<List<String>> {
        return Single.just(getRange(from))
    }
}