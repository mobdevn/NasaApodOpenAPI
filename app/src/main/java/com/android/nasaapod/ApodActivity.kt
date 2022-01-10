package com.android.nasaapod

import android.content.Context
import android.graphics.Bitmap
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import com.android.nasaapod.network.repository.ApiClientRepository

class ApodActivity : AppCompatActivity(), ApodInteractor.View {
    private lateinit var imageView: ImageView
    private lateinit var toolbar: Toolbar
    private lateinit var picDescription: TextView
    private lateinit var picTitle: TextView
    private lateinit var presenter: ApodInteractor.Presenter
    private lateinit var progressDialog: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apod)
        initViews()
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        presenter = ApodPresenter(
            ApiClientRepository(),
            connectivityManager,
            getPreferences(Context.MODE_PRIVATE),
            this
        )
        presenter.start()
    }

    override fun setTitle(title: String) {
        picTitle.text = title
    }

    override fun setDescription(description: String) {
        picDescription.text = description
    }

    override fun setPicOfTheDay(bitmap: Bitmap) {
        imageView.setImageBitmap(bitmap)
    }

    override fun stopProgressDialog() {
        progressDialog.visibility = View.GONE
    }

    override fun showProgressDialog() {
        progressDialog.visibility = View.VISIBLE
    }

    override fun getAbsolutePath(): String? {
        return getExternalFilesDir(null)?.absolutePath
    }

    override fun showNetworkErrorDialog() {
        AlertDialog.Builder(this)
            .setTitle("Network Error")
            .setMessage("We are not connected to the internet, showing you the last image we have.")
            .setNeutralButton("Ok") { dialog, which ->
                dialog.dismiss()
            }.show()
    }

    override fun showErrorDialog() {
        AlertDialog.Builder(this)
            .setTitle("Error")
            .setMessage("There was an error trying to get Picture of the day, please come back later")
            .setNeutralButton("Ok") { dialog, which ->
                dialog.dismiss()
                finish()
            }.show()
    }

    private fun initViews() {
        toolbar = findViewById(R.id.toolbar)
        imageView = findViewById(R.id.pic_of_the_day)
        picTitle = findViewById(R.id.pictitle)
        picDescription = findViewById(R.id.picdescription)
        progressDialog = findViewById(R.id.progressbar)
    }

}