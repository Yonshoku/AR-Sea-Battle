package com.PLbadcompany

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import java.util.ArrayList


class MainActivity : AppCompatActivity() , View.OnClickListener {

    private var LANButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        LANButton = findViewById(R.id.LANButton)
        LANButton?.setOnClickListener(this)
    }

    override fun onClick(v : View) {

        when (v.id) {
            R.id.LANButton ->  {
                startActivity(Intent(this, LANList::class.java))
            }
        }
    }
}

