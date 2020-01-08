package com.kishynskaya.testprojectrandomuser

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Error

class HomeActivity : AppCompatActivity(R.layout.activity_main), UsersAdapter.OnClickResult {

    private lateinit var usersViewModel: ViewModelUsersList
    private lateinit var adapter: UsersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        usersViewModel = ViewModelProviders.of(this).get(ViewModelUsersList::class.java)
        adapter = UsersAdapter()
        adapter.setOnclickListener(this)
        recyclerViewUsers.adapter = adapter
        subscribeUI()
    }

    private fun subscribeUI() {
        usersViewModel.error!!.observe(this,
            Observer<Error> {
                Toast.makeText(this, "Can't load data", Toast.LENGTH_LONG).show()
            })
        usersViewModel.userList!!.observe(this,
            Observer<PagedList<Result>> { data ->
                adapter.submitList(data)
            })
    }

    override fun onClickResult(result: Result) {
        val intent = Intent(this@HomeActivity, ProfileActivity::class.java)
        intent.putExtra("data", Gson().toJson(result))
        startActivity(intent)
    }
}
