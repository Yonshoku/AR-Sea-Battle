package com.PLbadcompany.core

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.PLbadcompany.R
import com.PLbadcompany.common.DialogHelper
import com.PLbadcompany.lan.ConnectionsListActivity

class MainMenuActivity : AppCompatActivity() {
    private var TAG = this::class.java.simpleName
    private lateinit var lanButton: Button
    private lateinit var singleGameButton: Button
    private lateinit var dialogHelper : DialogHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_menu_activity)

        lanButton = findViewById(R.id.lanButton)
        lanButton.setOnClickListener {
            startActivity(Intent(this, ConnectionsListActivity::class.java))
        }

        singleGameButton = findViewById(R.id.singleGameButton)
        singleGameButton.setOnClickListener {
            startActivity(Intent(this, ARCoreSessionActivity::class.java))
        }
    }

}

