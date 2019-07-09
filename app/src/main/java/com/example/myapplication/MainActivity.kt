package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val data = MutableLiveData<List<String>>()
    val repo = Repo()
    val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        data.observe(this, Observer {
            it.map { "$it \n" }.toList()
            container.text =  it.map { "$it \n" }.toList().toString()
        })
        repo.update()

        button.setOnClickListener {
            repo.loadMore()
        }
    }

    override fun onResume() {
        super.onResume()
        val d = repo.fetchItems().subscribe {
            data.postValue(it)
        }
        compositeDisposable.add(d)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}
