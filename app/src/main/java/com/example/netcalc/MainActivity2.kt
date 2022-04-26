package com.example.netcalc

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
    }
    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()

        val addressView = findViewById<TextView>(R.id.address)
        val address16View = findViewById<TextView>(R.id.address16)
        val bitmaskView = findViewById<TextView>(R.id.Bitmask)
        val netmaskView = findViewById<TextView>(R.id.netmask)
        val netmask16View = findViewById<TextView>(R.id.netmask16)
        val wildcardView = findViewById<TextView>(R.id.wildcard)
        val wildcard16View = findViewById<TextView>(R.id.wildcard16)

        val ip1String = intent.extras?.get("ip1").toString()
        val ip2String = intent.extras?.get("ip2").toString()
        val maskString = intent.extras?.get("mask").toString()

        var wildcard = ""
        try {
            val mask = ArrayList<UByte>()
            maskString.split(" - ")[1].split(".").forEach { b ->
                mask.add(b.toUByte())
            }
            mask.forEach { b ->  wildcard += "${b.inv()}." }
        } catch (e: Exception){ Log.d("MyLog", "$e") }
        try {
            val ip1 = ArrayList<UByte>()
            ip1String.split(".").forEach { b ->
                ip1.add(b.toUByte())
            }

        } catch (e: Exception){ Log.d("MyLog", "$e")}
        try {
            val ip2 = ArrayList<UByte>()
            ip2String.split(".").forEach { b ->
                ip2.add(b.toUByte())
            }

        } catch (e: Exception){ Log.d("MyLog", "$e") }

        //-----------------------
        addressView.text = "Address: $ip1String"
//        address16View.text = "${}" todo: 16-ричный
        bitmaskView.text = "Bitmask: ${maskString.split(" - ")[0]}"
        netmaskView.text = "Netmask: ${maskString.split(" - ")[1]}"
//        netmask16View.text = "${}" todo: 16-ричный
        wildcardView.text = "Wildcard: ${wildcard.removeSuffix(".")}"
//        wildcard16View.text = "" todo: 16-ричный

    }
}