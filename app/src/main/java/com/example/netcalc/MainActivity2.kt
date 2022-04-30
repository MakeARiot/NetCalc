package com.example.netcalc

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

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
        val broadcastView = findViewById<TextView>(R.id.broadcast)
        val broadcast16View = findViewById<TextView>(R.id.broadcast16)
        val firsthostView = findViewById<TextView>(R.id.firsthost)
        val firsthost16View = findViewById<TextView>(R.id.firsthost16)
        val lasthostView = findViewById<TextView>(R.id.lasthost)
        val lasthost16View = findViewById<TextView>(R.id.lasthost16)
        val networkView = findViewById<TextView>(R.id.network)
        val network16View = findViewById<TextView>(R.id.network16)

        val ip1String = intent.extras?.get("ip1").toString()
        val ip2String = intent.extras?.get("ip2").toString()
        val maskString = intent.extras?.get("mask").toString()

        var wildcard = ""
        var networks = ""
        val mask = ArrayList<UByte>()

        try {
            // маска
            maskString.split(" - ")[1].split(".").forEach { b ->
                mask.add(b.toUByte())
            }
        } catch (e: Exception) { Log.d("MyLog", "Mask $e") }

        try {
            // обратная маска
            mask.forEach { b ->  wildcard += "${b.inv()}." }
        } catch (e: Exception) { Log.d("MyLog", "Wildcard $e") }

        try {
            // 1 айпи
            val ip1 = ArrayList<UByte>()
            ip1String.split(".").forEach { b ->
                ip1.add(b.toUByte())
            }
        } catch (e: Exception) { Log.d("MyLog", "IP 1 $e") }

        try {
            // 2 айпи
            val ip2 = ArrayList<UByte>()
            ip2String.split(".").forEach { b ->
                ip2.add(b.toUByte())
            }
        } catch (e: Exception) { Log.d("MyLog", "IP 2 $e") }

        try {
            // адрес сети
            val maskb = ArrayList<UByte>()
            maskString.split(" - ")[1].split(".").forEach { b ->
                maskb.add(b.toUByte())
            }
            val ip1b = ArrayList<UByte>()
            ip1String.split(".").forEach { b ->
                ip1b.add(b.toUByte())
            }
            for (i in 0..3){
                networks += "${(maskb[i] and  ip1b[i])}."
            }

        } catch (e: Exception) { Log.d("MyLog", "network $e") }


        // -------------------- переделать
        // todo сделать широковещательный, первый, последний хосты, количество адресов
//        // широковещательный
//        val broadcast = ip1String.split(".").toMutableList()
//        broadcast[broadcast.size] = "255"
//        var broadcastStr = ""
//        broadcast.forEach {
//            broadcastStr += "$it."
//        }
//        // первый хост
//        val broadcast = ip1String.split(".").toMutableList()
//        broadcast[broadcast.size] = "255"
//        var broadcastStr = ""
//        broadcast.forEach {
//            broadcastStr += "$it."
//        }
//        // последний хост
//        val broadcast = ip1String.split(".").toMutableList()
//        broadcast[broadcast.size] = "255"
//        var broadcastStr = ""
//        broadcast.forEach {
//            broadcastStr += "$it."
//        }

        //-----------------------
        try {
            addressView.text = "Address: $ip1String"
//        address16View.text = "${}" todo: 16-ричный
            bitmaskView.text = "Bitmask: ${maskString.split(" - ")[0]}"
            netmaskView.text = "Netmask: ${maskString.split(" - ")[1]}"
//        netmask16View.text = "${}" todo: 16-ричный
            wildcardView.text = "Wildcard: ${wildcard.removeSuffix(".")}"
//        wildcard16View.text = "" todo: 16-ричный
//        broadcastView.text = "Broadcast: ${broadcastStr.removeSuffix(".")}"
//        broadcast16View.text = "" todo: 16-ричный
            networkView.text = "Network: ${networks.removeSuffix(".")}"

        } catch (e: Exception) { Log.d("MyLog", "$e") }
    }
}