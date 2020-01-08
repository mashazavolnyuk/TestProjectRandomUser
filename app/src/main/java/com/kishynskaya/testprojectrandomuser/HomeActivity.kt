package com.kishynskaya.testprojectrandomuser

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList

import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Error
import java.lang.Exception

class HomeActivity : AppCompatActivity(R.layout.activity_main) {

    private lateinit var usersViewModel: ViewModelUsersList
    var testList: PagedList<Result>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        usersViewModel = ViewModelProviders.of(this).get(ViewModelUsersList::class.java)
        fab.setOnClickListener {
            try {
                testList?.loadAround(100) //test pagination
            } catch (ex: Exception) {
                Log.d("Exception", ex.message)
            }
        }
        subscribeUI()
    }

    private fun subscribeUI() {
        usersViewModel.error!!.observe(this,
            Observer<Error> { error ->
                Toast.makeText(this, "Can't load data", Toast.LENGTH_LONG).show()
            })
        usersViewModel.userList!!.observe(this,
            Observer<PagedList<Result>> { data ->
                testList = data

            })
    }
}
