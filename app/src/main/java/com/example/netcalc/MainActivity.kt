package com.example.netcalc

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()

        try {
            val ip1tv: TextInputEditText = findViewById(R.id.textInputEdit1)
            val ip2tv: TextInputEditText = findViewById(R.id.textInputEdit2)
            val masktv: TextInputEditText = findViewById(R.id.textInputEditM)
            val restv: TextView = findViewById(R.id.res)
            val btn: Button = findViewById(R.id.button)
            masktv.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_CLASS_TEXT
            ip1tv.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_CLASS_TEXT
            ip2tv.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_CLASS_TEXT

            ip1tv.setText("192.168.1.1")
            ip2tv.setText("192.168.1.10")
            masktv.setText("255.255.255.0")

            btn.setOnClickListener {
                try{
                    isInOneNet(ip1tv, ip2tv, masktv, restv)
                } catch (e: Exception){ Log.d("MyLog", e.toString()) }

            }

        } catch (e: Exception){
            Log.d("MyLog", e.toString())
        }
    }
    fun isInOneNet(ip1tv: TextInputEditText, ip2tv: TextInputEditText,
                   masktv: TextInputEditText, restv: TextView){
        val mask = ArrayList<UByte>()
        masktv.text.toString().split(".").forEach { b ->
            mask.add(b.toUByte())
        }
        val ip1 = ArrayList<UByte>()
        ip1tv.text.toString().split(".").forEach { b ->
            ip1.add(b.toUByte())
        }
        val ip2 = ArrayList<UByte>()
        ip2tv.text.toString().split(".").forEach { b ->
            ip2.add(b.toUByte())
        }

        val listres1 = ArrayList<UByte>()
        val listres2 = ArrayList<UByte>()
        for (i in 0..3){
            listres1.add(mask[i] and  ip1[i])
            listres2.add(mask[i] and ip2[i])
        }
        Log.d("MyLog", listres1.toString())
        Log.d("MyLog", listres2.toString())

        if (listres1 == listres2){
            restv.text = "Узлы находятся в одной сети"
        } else restv.text = "Узлы находятся в разных сетях"

    }
}