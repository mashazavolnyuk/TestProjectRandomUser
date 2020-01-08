package com.kishynskaya.testprojectrandomuser

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_main.*

class HomeActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fab.setOnClickListener { view ->
           startActivity(Intent(this@HomeActivity, ProfileActivity::class.java))
        }
    }
}
