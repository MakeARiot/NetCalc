package com.example.netcalc

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.text.InputType
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader


class MainActivity() : AppCompatActivity() {
    var ip1tv: TextInputEditText? = null
    var ip2tv: TextInputEditText? = null
    var masktv: TextInputEditText? = null
    var restv: TextView? = null
    var btnOneNet: Button? = null
    var btnIp2: Button? = null
    var btnIp1: Button? = null
    var spinner: Spinner? = null

        @SuppressLint("SetTextI18n")
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)

            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

            val name = findViewById<TextView>(R.id.textView2)
            name.text = "${resources.getString(R.string.app_name)} IPv4"

            ip1tv = findViewById(R.id.textInputEdit1)
            ip2tv = findViewById(R.id.textInputEdit2)
            masktv = findViewById(R.id.textInputEditM)
            restv = findViewById(R.id.res)
            restv!!.movementMethod = ScrollingMovementMethod()
            btnOneNet = findViewById(R.id.button)
            btnIp2 = findViewById(R.id.button2)
            btnIp1 = findViewById(R.id.button5)
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
    override fun onResume(){
        super.onResume()

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        val ping1btn = findViewById<Button>(R.id.ping1)
        val ping2btn = findViewById<Button>(R.id.ping2)

        // ping 1
        ping1btn.setOnClickListener {
            if (ip1tv!!.text.toString() == "1.2.3.4.5"){
                Toast.makeText(this, "MADE BY kkramarskiy", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (!isNetworkAvailable()){
                Toast.makeText(applicationContext,
                    "Отсутствует соединение с интернетом", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            // асинхронный пинг, вывод результата в ScrollView
            CoroutineScope(Dispatchers.Main).launch {
                val ans = withContext(Dispatchers.Default){
                    ping(ip1tv!!.text.toString())
                }
                restv!!.text = ans
            }
        }
        // ping 2
        ping2btn.setOnClickListener {
            if (!isNetworkAvailable()){
                Toast.makeText(applicationContext, "Отсутствует соединение с интернетом", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            // асинхронный пинг, вывод результата в ScrollView
            CoroutineScope(Dispatchers.Main).launch {
                val ans = withContext(Dispatchers.Default){
                    ping(ip2tv!!.text.toString())
                }
                restv!!.text = ans
            }
        }

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
            btnOneNet!!.setOnClickListener {
                try{
                    if (ip2tv!!.text.toString() == "" && ip1tv!!.text.toString() == ""){
                        Toast.makeText(applicationContext, "Введи оба айпи", Toast.LENGTH_LONG).show()
                        return@setOnClickListener
                    }
                    Toast.makeText(applicationContext, isInOneNet(ip1tv!!, ip2tv!!, masktv!!, restv!!), Toast.LENGTH_LONG).show()
                } catch (e: Exception){ Log.d("MyLog", e.toString()) }
            }

            // вся инфа по сетям для 2 ip, переход на 2 экран
            btnIp2!!.setOnClickListener{
                Log.d("MyLog", "-----------1--------------")
                ip2tv!!.text = ip2tv!!.text
                try {
                    val ip = IPv4(spinner!!.selectedItem.toString())

                    val network = ip.getNetwork(spinner!!.selectedItem.toString().split(" - ")[1], ip2tv!!.text.toString())
                    val broadcast = ip.getBroadcast(spinner!!.selectedItem.toString().split(" - ")[1], network)

                    if (ip2tv!!.text.toString() == network || ip2tv!!.text.toString() == broadcast){
                        Toast.makeText(applicationContext, "ip является адресом сети или широковещательным адресом или выходит за границы сети", Toast.LENGTH_LONG).show()
                        return@setOnClickListener
                    }
                    if (ip2tv!!.text.toString() == ""){
                        Toast.makeText(applicationContext, "Введите айпи", Toast.LENGTH_LONG).show()
                        return@setOnClickListener
                    }
                    else{
                        val intent = Intent(applicationContext, MainActivity2::class.java)
                        intent.putExtra("mask", spinner!!.selectedItem.toString())
                        intent.putExtra("ip", ip2tv!!.text)
                        Log.d("MyLog", "-----------2--------------")
                        startActivity(intent)
                    }
                } catch (e: Exception){ Log.d("MyLog", "tyta $e") }
            }
            // вся инфа по сетям для 1 ip, переход на 2 экран
            btnIp1!!.setOnClickListener{
                Log.d("MyLog", "------------1-------------")

                try {
                    ip1tv!!.text = ip1tv!!.text
                } catch (e: Exception) { Log.d("MyLog", "$e") }
                try {
                    val ip = IPv4(spinner!!.selectedItem.toString())

                    val network = ip.getNetwork(spinner!!.selectedItem.toString().split(" - ")[1], ip1tv!!.text.toString())
                    val broadcast = ip.getBroadcast(spinner!!.selectedItem.toString().split(" - ")[1], network)

                    if (ip1tv!!.text.toString() == network || ip1tv!!.text.toString() == broadcast){
                        Toast.makeText(applicationContext,
                            "Ip является адресом сети или широковещательным адресом или выходит за границы сети",
                            Toast.LENGTH_LONG).show()
                        return@setOnClickListener
                    }
                    if (ip1tv!!.text.toString() == ""){
                        Toast.makeText(applicationContext, "Введите айпи", Toast.LENGTH_LONG).show()
                        return@setOnClickListener
                    }
                    else{
                        val intent = Intent(applicationContext, MainActivity2::class.java)
                        intent.putExtra("mask", spinner!!.selectedItem.toString())
                        intent.putExtra("ip", ip1tv!!.text)
                        Log.d("MyLog", "-----------2--------------")
                        startActivity(intent)
                    }
                } catch (e: Exception){ Log.d("MyLog", e.toString()) }
            }

        } catch (e: Exception){
            Log.d("MyLog", e.toString())
        }
    }

    // проверяет в одной ли сети два ip, сразу выводит результат в TextView
    fun isInOneNet(ip1tv: TextInputEditText, ip2tv: TextInputEditText,
                   masktv: TextInputEditText, restv: TextView): String {
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

        return if (listres1 == listres2){
            "Узлы находятся в одной сети"
        } else "Узлы находятся в разных сетях"
    }

    // возвращает текст запроса по ip
    fun ping(ip: String): String {
        var str = ""
        try {
            val process = Runtime.getRuntime().exec(
                "/system/bin/ping -c 8 $ip"
            )
            val reader = BufferedReader(
                InputStreamReader(
                    process.inputStream
                )
            )
            var i: Int
            val buffer = CharArray(4096)
            val output = StringBuffer()
            while (reader.read(buffer).also { i = it } > 0) output.append(buffer, 0, i)
            reader.close()

            str = output.toString()

        } catch (e: IOException) {
            Log.d("MyLog", "ping $e")
        }
        return str
    }

    // возвращает true если есть подключение к инету
    fun isNetworkAvailable(): Boolean {
        val manager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = manager.activeNetworkInfo
        var isAvailable = false
        if (networkInfo != null && networkInfo.isConnected) {
            isAvailable = true
        }
        return isAvailable
    }
    // FFFFFFFFFFFFFFFFFFF
}
