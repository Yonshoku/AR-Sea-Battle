package com.PLbadcompany.core

import android.os.Bundle
import android.widget.SeekBar

import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.PLbadcompany.R


class SinglePlayerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.single_player)

        val seekBar1 = findViewById<SeekBar>(R.id.seekBar1)
        val textView1 = findViewById<TextView>(R.id.seekBarValue1)
        seekBar1.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                textView1.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar1: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar1: SeekBar?) {}
        })

        val seekBar2 = findViewById<SeekBar>(R.id.seekBar2)
        val textView2 = findViewById<TextView>(R.id.seekBarValue2)
        seekBar2.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                textView2.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar2: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar2: SeekBar?) {}
        })
    }



    override fun onResume() {
        super.onResume()

    }
}