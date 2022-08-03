package com.example.netcalc

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import kotlin.math.pow

class MainActivity2 : AppCompatActivity() {

    var addressView: TextView? = null
    var address16View: TextView? = null
    var bitmaskView: TextView? = null
    var netmaskView: TextView? = null
    var netmask16View: TextView? = null
    var wildcardView: TextView? = null
    var wildcard16View: TextView? = null
    var broadcastView: TextView? = null
    var broadcast16View: TextView? = null
    var firsthostView: TextView? = null
    var firsthost16View: TextView? = null
    var lasthostView: TextView? = null
    var lasthost16View: TextView? = null
    var networkView: TextView? = null
    var network16View: TextView? = null
    var subnetsnumberView: TextView? = null
    var hostsnumberView: TextView? = null

    var adrBin: TextView? = null
    var netmaskBin: TextView? = null
    var wildcardBin: TextView? = null
    var broadcastBin: TextView? = null
    var firstHostBin: TextView? = null
    var lastHostBin: TextView? = null
    var networkBin: TextView? = null

    var ipString: String? = null
    var maskString: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
    }

    @SuppressLint("SetTextI18n", "ResourceType")
    override fun onResume() {
        super.onResume()

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        addressView = findViewById<TextView>(R.id.address)
        address16View = findViewById<TextView>(R.id.addrHex)
        bitmaskView = findViewById<TextView>(R.id.Bitmask)
        netmaskView = findViewById<TextView>(R.id.netmask)
        netmask16View = findViewById<TextView>(R.id.netmaskHex)
        wildcardView = findViewById<TextView>(R.id.wildcard)
        wildcard16View = findViewById<TextView>(R.id.wildcardHex)
        broadcastView = findViewById<TextView>(R.id.broadcast)
        broadcast16View = findViewById<TextView>(R.id.broadcastHex)
        firsthostView = findViewById<TextView>(R.id.firsthost)
        firsthost16View = findViewById<TextView>(R.id.firsthostHex)
        lasthostView = findViewById<TextView>(R.id.lasthost)
        lasthost16View = findViewById<TextView>(R.id.lasthostHex)
        networkView = findViewById<TextView>(R.id.network)
        network16View = findViewById<TextView>(R.id.networkHex)
        subnetsnumberView = findViewById<TextView>(R.id.subnetsnumber)
        hostsnumberView = findViewById<TextView>(R.id.hostsnumber)

        adrBin = findViewById(R.id.addrBin)
        netmaskBin = findViewById(R.id.netmaskBin)
        wildcardBin = findViewById(R.id.wildcardBin)
        broadcastBin = findViewById(R.id.broadcastBin)
        firstHostBin = findViewById(R.id.firsthostBin)
        lastHostBin = findViewById(R.id.lasthostBin)
        networkBin = findViewById(R.id.networkBin)

        ipString = intent.extras?.get("ip").toString()
        maskString = intent.extras?.get("mask").toString()

        val ip = IPv4(maskString!!)

        val mask = ip.convertMaskToUByteList()
        val wildcard = ip.getWildcard(mask).removeSuffix(".")
        val bitmask = maskString!!.split(" - ")[0]
        val netmask = maskString!!.split(" - ")[1]
        val network = ip.getNetwork(netmask, ipString!!).removeSuffix(".")
        val sub_hos = ip.getNumberOfSubnetsHosts(netmask)
        val broadcast = ip.getBroadcast(netmask, network)
        val firsthost = ip.getFirstHost(network, sub_hos!![1])
        val lasthost = ip.getLastHost(broadcast, sub_hos[1])

        val maskHex = ip.getHex(netmask)
        val addrHex = ip.getHex(ipString!!)
        val wildcardHex = ip.getHex(wildcard)
        val broadcasthex = ip.getHex(broadcast)
        val firsthostHex = ip.getHex(firsthost)
        val lasthostHex = ip.getHex(lasthost)
        val networkhex = ip.getHex(network)
        val maskNet = ip.getMsNw(netmask, network)

        try {
            addressView!!.text = "$ipString"
            bitmaskView!!.text = bitmask
            netmaskView!!.text = netmask
            wildcardView!!.text = wildcard
            broadcastView!!.text = broadcast
            networkView!!.text = network
            subnetsnumberView!!.text = sub_hos[0]
            hostsnumberView!!.text = sub_hos[1]
            firsthostView!!.text = firsthost
            lasthostView!!.text = lasthost

            address16View!!.text = addrHex
            netmask16View!!.text = maskHex
            wildcard16View!!.text = wildcardHex
            broadcast16View!!.text = broadcasthex
            network16View!!.text = networkhex
            firsthost16View!!.text = firsthostHex
            lasthost16View!!.text = lasthostHex

            adrBin!!.text = ip.getBin(ipString!!)
            netmaskBin!!.text = maskNet[0]
            networkBin!!.text = maskNet[1]
            wildcardBin!!.text = ip.getBin(wildcard)
            broadcastBin!!.text = ip.getBin(broadcast)
            firstHostBin!!.text = ip.getBin(firsthost)
            lastHostBin!!.text = ip.getBin(lasthost)

        } catch (e: Exception) { Log.d("MyLog", "$e") }
    }
}

class IPv4(private val maskString: String){

    // возвращает ArrayList<String> количество подсетей[0], количество хостов[1]
    fun getNumberOfSubnetsHosts(netmask: String): ArrayList<String>? {
        try {
            val net = Array(4) { "" }
            var binnetmaskstr = ""
            val list = ArrayList<Int>()
            Log.d("MyLog", """getNumberOfSubnetsHosts: 
                |mask $maskString""".trimMargin())

            netmask.split(".").forEach { list.add(it.toInt()) }

            for (i in 0 until 4) {
                var str = convStrToBin(list[i])
                if (str.length <= 8) {
                    for (j in 0 until 8 - str.length) {
                        str += "0"
                    }
                }
                binnetmaskstr += str
            }

            var ind1 = 0
            var ind2 = 8
            var onecounter = 0
            var zerocounter = 0
            for (i in 0 until 4) {
                var temp = ""
                for (j in ind1 until ind2) {
                    temp += binnetmaskstr[j]
                }
                ind1 += 8
                ind2 += 8
                net[i] = temp
            }
            for (i in net) {
                if ('0' in i) {
                    for (j in i) {
                        if (j == '1') {
                            onecounter++
                        } else zerocounter++
                    }
                }
            }

            var hosts: Long = 0
            val base = 2
            val subnets = base.toDouble().pow(onecounter).toInt()

            for (i in 0 until zerocounter - 1) {
                if (i == 0) {
                    hosts += 2
                }
                hosts *= base
            }
            hosts -= 2
            Log.d("MyLog", """getNumberOfSubnetsHosts:
                |subn: $subnets hosts: $hosts""".trimMargin())
            if (hosts < 0) {
                hosts = 0
            }
            val res = ArrayList<String>()
            res.add("$subnets")
            res.add("$hosts")
            return res

        } catch (e: java.lang.Exception) { Log.d("MyLog", "$e") }

        return null
    }

    // возвращает String broadcast в десятичном виде
    fun getBroadcast(netmask: String, network: String): String {
        val maskUByteList =
            convertUByteListToBinStringList(convertIpToUByteList(netmask))
                .toString().removeSuffix("]").removePrefix("[")
                .replace(", ", ".")
        val ipUByteList =
            convertUByteListToBinStringList(convertIpToUByteList(network))
                .toString().removeSuffix("]").removePrefix("[")
                .replace(", ", ".")

        Log.d("MyLog", """getBroadcast:
            mask $maskUByteList 
            network $ipUByteList
        """.trimMargin())

        var index = 0
        for (i in maskUByteList.indices) {
            index = maskUByteList.indexOf('0')
        }
        val sb = StringBuilder(ipUByteList).also {
            for (i in ipUByteList.indices) {
                if (i >= index) {
                    if (maskUByteList[i] == '.') {
                        it.setCharAt(i, '.')
                    } else {
                        it.setCharAt(i, '1')
                    }
                }
            }
        }
        val broadcast = sb.toString() // двоичное представление бродкаста
        Log.d("MyLog", """getBroadcast: 
            |broadcast $broadcast""".trimMargin())

        val brDecList = ArrayList<UByte>()
        broadcast.split(".").forEach { brDecList.add(it.toInt(2).toUByte()) }
        val brDec = brDecList.toString().removeSuffix("]").removePrefix("[")
            .replace(", ", ".")
        Log.d("MyLog", """getBroadcast: 
            |broadcast Int $brDecList""".trimMargin())

        return brDec
    }

    // принимает num: Int, возвращет : String в двоичном виде
    private fun convStrToBin(num: Int): String {
        var binNum = ""
        try {
            if (num == 0) {
                return num.toString()
            }

            var quotient = num
            while (quotient > 0) {
                val remainder = quotient % 2
                binNum += remainder.toString()
                quotient /= 2
            }
            binNum = binNum.reversed()
            return binNum
        } catch (e: Exception) { Log.d("MyLog", "conv $e") }

        return binNum
    }

    // принимает ArrayList<UByte>, возвращает ArrayList<String> в двоичном виде
    fun convertUByteListToBinStringList(uByteList: ArrayList<UByte>): ArrayList<String> {
        val list = ArrayList<String>(4)
        uByteList.forEach { num ->
            try {
                var binNum = ""
                var quotient = num.toInt()

                if (num.toInt() == 0) {
                    binNum = "00000000"
                    list.add(binNum)
                    return@forEach
                }

                while (quotient > 0) {
                    val remainder = quotient % 2
                    binNum += remainder.toString()
                    quotient /= 2
                }

                if (binNum.length < 8) {
                    while (binNum.length < 8) {
                        binNum += "0"
                    }
                }
                binNum = binNum.reversed()
                list.add(binNum)

            } catch (e: Exception) { Log.d("MyLog", "uByteToBin $e") }
        }

        return list
    }

    // возвращает маску ArrayList<UByte> в десятичном виде
    fun convertMaskToUByteList(): ArrayList<UByte> {
        val mask = ArrayList<UByte>()
        try {
            maskString.split(" - ")[1].split(".").forEach { b ->
                mask.add(b.toUByte())
            }
            return mask

        } catch (e: Exception) { Log.d("MyLog", "Mask $e") }

        return mask
    }

    // принимает маску в ArrayList<UByte>, возвращает String wildcard в десятичном виде
    fun getWildcard(mask: ArrayList<UByte>): String {
        var wildcard = ""
        try {
            // обратная маска
            mask.forEach { b -> wildcard += "${b.inv()}." }
            return wildcard

        } catch (e: Exception) { Log.d("MyLog", "Wildcard $e") }

        return wildcard
    }

    // принимает ipString String, возвращает ArrayList<UByte> в десятичном виде
    fun convertIpToUByteList(ipString: String): ArrayList<UByte> {
        val ip = ArrayList<UByte>()
        try {
            ipString.split(".").forEach { b ->
                ip.add(b.toUByte())
            }
            return ip

        } catch (e: Exception) { Log.d("MyLog", "getIpToUByteList $e") }

        return ip
    }

    // принимает netMask: String, ipString: String, возвращает network: String в десятичном виде
    fun getNetwork(netMask: String, ipString: String): String {
        var network = ""
        try {
            val maskb = ArrayList<UByte>()
            netMask.split(".").forEach { b ->
                maskb.add(b.toUByte())
            }
            val ip1b = ArrayList<UByte>()
            ipString.split(".").forEach { b ->
                ip1b.add(b.toUByte())
            }
            for (i in 0..3) {
                network += "${(maskb[i] and ip1b[i])}."
            }
            return network.removeSuffix(".")

        } catch (e: Exception) { Log.d("MyLog", "network $e") }

        return network
    }

    // принимает network: String, hosts: String, возвращает firstHost: String в десятичном виде
    fun getFirstHost(network: String, hosts: String): String {
        if (hosts == "0") {
            return "No hosts available"
        }
        var firstHost = ""

        val netList: ArrayList<String> = network.split(".") as ArrayList<String>
        val new = netList[3].toUByte() + 1u
        netList[3] = new.toString()
        netList.forEach {
            firstHost += "$it."
        }
        firstHost = firstHost.removeSuffix(".")
        Log.d("MyLog", """getFirstHost: 
            |firstHost $firstHost""".trimMargin())

        return firstHost
    }

    // принимает broadcast: String, hosts: String, возвращает lastHost: String в десятичном виде
    fun getLastHost(broadcast: String, hosts: String): String {
        if (hosts == "0") {
            return "No hosts available"
        }
        var lastHost = ""

        val netList: ArrayList<String> = broadcast.split(".") as ArrayList<String>
        val new = netList[3].toUByte() - 1u
        netList[3] = new.toString()
        netList.forEach {
            lastHost += "$it."
        }
        lastHost = lastHost.removeSuffix(".")
        Log.d("MyLog", """getLastHost: 
            |getLastHost $lastHost""".trimMargin())

        return lastHost
    }

    // принимает string: String, возвращает String в шеснадцатиричном виде
    fun getHex(string: String): String {
        try {
            val list = ArrayList<String>()
            string.split(".").forEach { list.add(it.toLong().toString(16)) }

            return list.toString().removeSuffix("]")
                .removePrefix("[").replace(", ", ":").uppercase(Locale.ROOT)

        } catch (e: Exception) { Log.d("MyLog", "getHex $e") }

        return ""
    }

    // принимает string: String, возвращает String в двоичном виде
    fun getBin(string: String): String {
        val list = ArrayList<UByte>()
        string.split(".").forEach { list.add(it.toUByte()) }

        return convertUByteListToBinStringList(list).toString().removePrefix("[")
            .removeSuffix("]").replace(", ", ".")
    }

    // принимает netmask: String, network: String в 1 экрана, возвращает ArrayList<String> netmask, network в двоичном виде
    fun getMsNw(netmask: String, network: String): ArrayList<String> {
        val res = ArrayList<String>(2)
        val maskStrBin =
            convertUByteListToBinStringList(convertIpToUByteList(netmask))
                .toString().removeSuffix("]").removePrefix("[")
                .replace(", ", ".")
        val netwStrBin =
            convertUByteListToBinStringList(convertIpToUByteList(network))
                .toString().removeSuffix("]").removePrefix("[")
                .replace(", ", ".")
        res.add(maskStrBin)
        res.add(netwStrBin)
        return res
    }
}
