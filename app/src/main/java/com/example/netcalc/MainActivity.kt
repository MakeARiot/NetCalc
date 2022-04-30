package com.example.netcalc

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {

    var ip1tv: TextInputEditText? = null
    var ip2tv: TextInputEditText? = null
    var masktv: TextInputEditText? = null
    var restv: TextView? = null
    var btn: Button? = null
    var btn2: Button? = null
    var spinner: Spinner? = null

        override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

        ip1tv= findViewById(R.id.textInputEdit1)
        ip2tv = findViewById(R.id.textInputEdit2)
        masktv = findViewById(R.id.textInputEditM)
        restv = findViewById(R.id.res)
        btn = findViewById(R.id.button)
        btn2 = findViewById(R.id.button2)
        masktv!!.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_CLASS_TEXT
        ip1tv!!.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_CLASS_TEXT
        ip2tv!!.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_CLASS_TEXT
        // начальные значения
        ip1tv!!.setText("192.168.1.1")
        ip2tv!!.setText("192.168.1.10")

        val data = Array<String>(33){""}
        data[0] = "/0 - 0.0.0.0"
        data[1] = "/1 - 128.0.0.0"
        data[2] = "/2 - 192.0.0.0"
        data[3] = "/3 - 224.0.0.0"
        data[4] = "/4 - 240.0.0.0"
        data[5] = "/5 - 248.0.0.0"
        data[6] = "/6 - 252.0.0.0"
        data[7] = "/7 - 254.0.0.0"
        data[8] = "/8 - 255.0.0.0"
        data[9] = "/9 - 255.128.0.0"
        data[10] = "/10 - 255.192.0.0"
        data[11] = "/11 - 255.224.0.0"
        data[12] = "/12 - 255.240.0.0"
        data[13] = "/13 - 255.248.0.0"
        data[14] = "/14 - 255.252.0.0"
        data[15] = "/15 - 255.254.0.0"
        data[16] = "/16 - 255.255.0.0"
        data[17] = "/17 - 255.255.128.0"
        data[18] = "/18 - 255.255.192.0"
        data[19] = "/19 - 255.255.224.0"
        data[20] = "/20 - 255.255.240.0"
        data[21] = "/21 - 255.255.248.0"
        data[22] = "/22 - 255.255.252.0"
        data[23] = "/23 - 255.255.254.0"
        data[24] = "/24 - 255.255.255.0"
        data[25] = "/25 - 255.255.255.128"
        data[26] = "/26 - 255.255.255.192"
        data[27] = "/27 - 255.255.255.224"
        data[28] = "/28 - 255.255.255.240"
        data[29] = "/29 - 255.255.255.248"
        data[30] = "/30 - 255.255.255.252"
        data[31] = "/31 - 255.255.255.254"
        data[32] = "/32 - 255.255.255.255"

        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner = findViewById<Spinner>(R.id.spinner)
        spinner!!.adapter = adapter
        spinner!!.prompt = "Mask"

    }

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()


        try {

            // выставить маску
            spinner!!.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    masktv!!.setText(spinner!!.selectedItem.toString().split(" - ")[1])
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
            // рассчитать из одной ли сети адреса
            btn!!.setOnClickListener {
                try{
                    isInOneNet(ip1tv!!, ip2tv!!, masktv!!, restv!!)
                } catch (e: Exception){ Log.d("MyLog", e.toString()) }
            }
            // вся инфа по сетям
            btn2!!.setOnClickListener{
                try {
                    ip1tv!!.text = ip1tv!!.text
                    ip2tv!!.text = ip2tv!!.text
                } catch (e: Exception) { Log.d("MyLog", "ничего не понял, но очень интересно") }
                try {
                    val intent = Intent(this, MainActivity2::class.java)
                    intent.putExtra("mask", spinner!!.selectedItem.toString())
                    intent.putExtra("ip1", ip1tv!!.text)
                    intent.putExtra("ip2", ip2tv!!.text)
                    startActivity(intent)
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