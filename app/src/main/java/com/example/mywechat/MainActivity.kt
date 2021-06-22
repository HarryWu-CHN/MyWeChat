package com.example.mywechat;


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun jumpToUser(username: String) {
        (application as App).username = username
        val intent: Intent = Intent(this, UserActivity::class.java)
        startActivity(intent)
        finish()
    }
}