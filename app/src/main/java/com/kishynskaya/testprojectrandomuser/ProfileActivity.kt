package com.kishynskaya.testprojectrandomuser

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity(R.layout.activity_profile) {

    var currentResult: Result? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val value = intent?.extras?.getString("data")
        try {
            currentResult = Gson().fromJson<Result>(value, Result::class.java)
            updateUI(currentResult!!)
        } catch (e: JsonSyntaxException) {}
    }

    private fun updateUI(result: Result) {
        Glide
            .with(this)
            .load(result.picture.large)
            .transform(CircleCrop())
            .into(imageViewBigLogo)

        val textTitle = result.name.run { "$first $last" }
        val skype = result.location.run { "$country, $city" }
        textViewNameSurname.text = textTitle
        textInputSkype.setText(skype)
        textInputSkype.setOnTouchListener { v, event -> true }
        textViewOld.text = getString(R.string.years, result.dob.age)
        textInputEmail.setText(result.email)
        textInputEmail.setOnTouchListener { v, event -> true }
        textInputPhone.apply {
            setText(result.cell)
            setOnTouchListener { v, event ->
                if (checkIfAlreadyHavePermission()) {
                    startActivity(Intent(Intent.ACTION_CALL, Uri.parse("tel:" + result.cell)))
                } else {
                    requestForSpecificPermission()
                }
                true
            }
        }
    }

    private fun checkIfAlreadyHavePermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun requestForSpecificPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(Manifest.permission.CALL_PHONE), 101
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) = when (requestCode) {
        101 -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startActivity(Intent(Intent.ACTION_CALL, Uri.parse("tel:" + currentResult!!.cell)))
        } else {
            Toast.makeText(this, "Permission Required", Toast.LENGTH_LONG).show()
        }
        else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}