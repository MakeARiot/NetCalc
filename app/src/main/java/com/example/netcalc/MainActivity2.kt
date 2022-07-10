package com.example.netcalc

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
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
    var subnetsnumber16View: TextView? = null
    var hostsnumberView: TextView? = null
    var hostsnumber16View: TextView? = null

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
    }
    @SuppressLint("SetTextI18n", "ResourceType")
    override fun onResume() {
        super.onResume()

        addressView = findViewById<TextView>(R.id.address)
        address16View = findViewById<TextView>(R.id.address16)
        bitmaskView = findViewById<TextView>(R.id.Bitmask)
        netmaskView = findViewById<TextView>(R.id.netmask)
        netmask16View = findViewById<TextView>(R.id.netmask16)
        wildcardView = findViewById<TextView>(R.id.wildcard)
        wildcard16View = findViewById<TextView>(R.id.wildcard16)
        broadcastView = findViewById<TextView>(R.id.broadcast)
        broadcast16View = findViewById<TextView>(R.id.broadcast16)
        firsthostView = findViewById<TextView>(R.id.firsthost)
        firsthost16View = findViewById<TextView>(R.id.firsthost16)
        lasthostView = findViewById<TextView>(R.id.lasthost)
        lasthost16View = findViewById<TextView>(R.id.lasthost16)
        networkView = findViewById<TextView>(R.id.network)
        network16View = findViewById<TextView>(R.id.network16)
        subnetsnumberView = findViewById<TextView>(R.id.subnetsnumber)
        subnetsnumber16View = findViewById<TextView>(R.id.subnetsnumber16)
        hostsnumberView = findViewById<TextView>(R.id.hostsnumber)
        hostsnumber16View = findViewById<TextView>(R.id.hostsnumber16)

        adrBin = findViewById(R.id.addrBin)
        netmaskBin = findViewById(R.id.netmaskBin)
        wildcardBin = findViewById(R.id.wildcardBin)
        broadcastBin = findViewById(R.id.broadcastBin)
        firstHostBin = findViewById(R.id.firsthostBin)
        lastHostBin = findViewById(R.id.lasthostBin)
        networkBin = findViewById(R.id.networkBin)

        ipString = intent.extras?.get("ip").toString()
        maskString = intent.extras?.get("mask").toString()

        val mask = getMaskUByteList()
        val wildcard = getWildcard(mask).removeSuffix(".")
        val bitmask = maskString!!.split(" - ")[0]
        val netmask = maskString!!.split(" - ")[1]
        val network = getNetwork().removeSuffix(".")
        val sub_hos = getNumberOfSubnetsHosts(netmask)
        val broadcast = getBroadcast(netmask, network)
        val firsthost = getFirstHost(network, sub_hos!![1])
        val lasthost = getLastHost(broadcast, sub_hos[1])

        val maskNet = getMsNw(netmask, network)

        try {
            addressView!!.text = "${resources.getString(R.string.addr)}$ipString"
//        address16View!!.text = "${}"
            bitmaskView!!.text = "${resources.getText(R.string.bitmask)}$bitmask"
            netmaskView!!.text = "${resources.getText(R.string.netmask)}$netmask"
//        netmask16View!!.text = "${}"
            wildcardView!!.text = "${resources.getText(R.string.wildcard)}${wildcard}"
//        wildcard16View!!.text = ""
            broadcastView!!.text = "${resources.getText(R.string.broadcast)}${broadcast}"
//        broadcast16View!!.text = ""
            networkView!!.text = "${resources.getText(R.string.network)}${network}"
//            network16View!!.text = ""
            subnetsnumberView!!.text = "${resources.getText(R.string.subnets)}${sub_hos[0]}"
//            subnetsnumber16View!!.text = ""
            hostsnumberView!!.text = "${resources.getText(R.string.hosts)}${sub_hos[1]}"
//            hostsnumber16View!!.text = ""
            firsthostView!!.text = "${resources.getText(R.string.firsthost)}${firsthost}"
//            firsthost16View!!.text = "First:\t${}"
            lasthostView!!.text = "${resources.getText(R.string.lasthost)}${lasthost}"
//            lasthost16View!!.text = "First:\t${}"

            adrBin!!.text = getBin(ipString!!)
            netmaskBin!!.text = maskNet[0]
            networkBin!!.text = maskNet[1]
            wildcardBin!!.text = getBin(wildcard)
            broadcastBin!!.text = getBin(broadcast)
            firstHostBin!!.text = getBin(firsthost)
            lastHostBin!!.text = getBin(lasthost)

        } catch (e: Exception) {
            Log.d("MyLog", "$e")
        }
    }

    private fun getNumberOfSubnetsHosts(netmask: String): ArrayList<String>? {
        try {
            val net = Array(4){""}
            var binnetmaskstr = ""
            val list = ArrayList<Int>()
            Log.d("MyLog", netmask)

            netmask.split(".").forEach { list.add(it.toInt()) }

            for (i in 0 until 4){
                var str = convStrToBin(list[i])
                if (str.length <= 8){
                    for (i in 0 until 8 - str.length){
                        str += "0"
                    }
                }
                binnetmaskstr += str
            }

            var ind1 = 0
            var ind2 = 8
            var onecounter = 0
            var zerocounter = 0
            for (i in 0 until 4){
                var temp = ""
                for (j in ind1 until ind2){
                    temp += binnetmaskstr[j]
                }
                ind1 += 8
                ind2 += 8
                net[i] = temp
            }
            for (i in net){
                if ('0' in i){
                    for (j in i){
                        if (j == '1'){
                            onecounter++
                        } else zerocounter++
                    }
                }
            }
            Log.d("MyLog", "ones: $onecounter zeros: $zerocounter")
            var subnets = 0
            var hosts: Long = 0
            val base = 2

            subnets = base.toDouble().pow(onecounter).toInt()

            for (i in 0 until zerocounter-1){
                if (i == 0) {
                    hosts += 2
                }
                hosts *= base
            }
            hosts -= 2
            Log.d("MyLog", "subn: $subnets hosts: $hosts")
            if (hosts < 0){
                hosts = 0
            }
            val res = ArrayList<String>()
            res.add("$subnets")
            res.add("$hosts")
            return res

        } catch (e: java.lang.Exception) { Log.d("MyLog", "$e") }
        return null
    }

    fun getBroadcast(netmask: String, network: String): String {
        val maskUByteList =
            getUByteListToBinStringList(getIpToUByteList(netmask))
                .toString().removeSuffix("]").removePrefix("[")
                .replace(", ", ".")
        val ipUByteList =
            getUByteListToBinStringList(getIpToUByteList(network))
                .toString().removeSuffix("]").removePrefix("[")
                .replace(", ", ".")

        Log.d("MyLog", "mask ${maskUByteList}")
        Log.d("MyLog", "network ${ipUByteList}")

        var index = 0
        for (i in maskUByteList.indices){
            index = maskUByteList.indexOf('0')
        }
        val sb = StringBuilder(ipUByteList).also {
            for (i in ipUByteList.indices){
                if (i >= index){
                    if (maskUByteList[i] == '.'){
                        it.setCharAt(i, '.')
                    } else {
                        it.setCharAt(i, '1')
                    }
                }
            }
        }
        val broadcast = sb.toString() // двоичное представление бродкаста
        Log.d("MyLog", "broadcast ${broadcast}")

        val brDecList = ArrayList<UByte>()
        broadcast.split(".").forEach { brDecList.add(it.toInt(2).toUByte()) }
        val brDec = brDecList.toString().removeSuffix("]").removePrefix("[")
            .replace(", ", ".")
        Log.d("MyLog", "broadcast Int ${brDecList}")

        return  brDec
    }

    private fun convStrToBin(num: Int): String{
        var binNum = ""
        try {
            if (num == 0){
                return num.toString()
            }

            var quotient = num
            while (quotient > 0){
                val remainder = quotient % 2
                binNum += remainder.toString()
                quotient /= 2
            }
            binNum = binNum.reversed()
            return binNum
        } catch (e: Exception) { Log.d("MyLog", "conv $e") }
        return binNum
    }

    fun getUByteListToBinStringList(uByteList: ArrayList<UByte>): ArrayList<String>{
        val list = ArrayList<String>(4)
        uByteList.forEach { num ->
            try {
                var binNum = ""
                var quotient = num.toInt()

                if (num.toInt() == 0){
                    binNum = "00000000"
                    list.add(binNum)
                    return@forEach
                }

                while (quotient > 0){
                    val remainder = quotient % 2
                    binNum += remainder.toString()
                    quotient /= 2
                }

                if (binNum.length < 8){
                    while (binNum.length < 8){
                        binNum += "0"
                    }
                }
                binNum = binNum.reversed()
                list.add(binNum)

            } catch (e: Exception) { Log.d("MyLog", "uByteToBin $e") }
        }

        return list
    }

    fun getMaskUByteList(): ArrayList<UByte>{
        val mask = ArrayList<UByte>()

        try {
            maskString!!.split(" - ")[1].split(".").forEach { b ->
                mask.add(b.toUByte())
            }
            return mask
        } catch (e: Exception) {
            Log.d("MyLog", "Mask $e")
        }
        return mask
    }

    fun getWildcard(mask: ArrayList<UByte>): String{
        var wildcard = ""

        try {
            // обратная маска
            mask.forEach { b -> wildcard += "${b.inv()}." }
            return wildcard
        } catch (e: Exception) {
            Log.d("MyLog", "Wildcard $e")
        }
        return wildcard
    }

    fun getIpToUByteList(ipString: String): ArrayList<UByte>{
        val ip = ArrayList<UByte>()
        try {
            ipString.split(".").forEach { b ->
                ip.add(b.toUByte())
            }
            return ip
        } catch (e: Exception) {
            Log.d("MyLog", "getIpToUByteList $e")
        }
        return ip
    }

    fun getNetwork(): String{
        var network = ""

        try {
            val maskb = ArrayList<UByte>()
            maskString!!.split(" - ")[1].split(".").forEach { b ->
                maskb.add(b.toUByte())
            }
            val ip1b = ArrayList<UByte>()
            ipString!!.split(".").forEach { b ->
                ip1b.add(b.toUByte())
            }
            for (i in 0..3) {
                network += "${(maskb[i] and ip1b[i])}."
            }
            return network
        } catch (e: Exception) {
            Log.d("MyLog", "network $e")
        }
        return network
    }

    fun getFirstHost(network: String, hosts: String): String{
        if (hosts == "0"){
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
        Log.d("MyLog", "getFirstHost $firstHost")
        return firstHost
    }

    fun getLastHost(broadcast: String, hosts: String): String{
        if (hosts == "0"){
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
        Log.d("MyLog", "getLastHost $lastHost")
        return lastHost
    }

    fun getBin(string: String): String{
        val list = ArrayList<UByte>()
        string!!.split(".").forEach { list.add(it.toUByte()) }

        return getUByteListToBinStringList(list).toString().removePrefix("[")
            .removeSuffix("]").replace(", ", ".")
    }

//    fun getAddressBin(): String{
//        val list = ArrayList<UByte>()
//        ipString!!.split(".").forEach { list.add(it.toUByte()) }
//
//        return getUByteListToBinStringList(list).toString().removePrefix("[")
//            .removeSuffix("]").replace(", ", ".")
//    }
//
//    fun getWildcardBin(wildcard: String): String{
//        val list = ArrayList<UByte>()
//        wildcard.split(".").forEach { list.add(it.toUByte()) }
//
//        return getUByteListToBinStringList(list).toString().removePrefix("[")
//            .removeSuffix("]").replace(", ", ".")
//    }
//
//    fun getBroadcastBin(broadcast: String): String{
//        val list = ArrayList<UByte>()
//        broadcast.split(".").forEach { list.add(it.toUByte()) }
//
//        return getUByteListToBinStringList(list).toString().removePrefix("[")
//            .removeSuffix("]").replace(", ", ".")
//    }
//
//    fun getFirstHostBin(first: String): String{
//        val list = ArrayList<UByte>()
//        first.split(".").forEach { list.add(it.toUByte()) }
//
//        return getUByteListToBinStringList(list).toString().removePrefix("[")
//            .removeSuffix("]").replace(", ", ".")
//    }
//
//    fun getlastHostBin(last: String): String{
//        val list = ArrayList<UByte>()
//        last.split(".").forEach { list.add(it.toUByte()) }
//
//        return getUByteListToBinStringList(list).toString().removePrefix("[")
//            .removeSuffix("]").replace(", ", ".")
//    }

    fun getMsNw(netmask: String, network: String): ArrayList<String> {
        val res = ArrayList<String>(2)
        val maskStrBin =
            getUByteListToBinStringList(getIpToUByteList(netmask))
                .toString().removeSuffix("]").removePrefix("[")
                .replace(", ", ".")
        val netwStrBin =
            getUByteListToBinStringList(getIpToUByteList(network))
                .toString().removeSuffix("]").removePrefix("[")
                .replace(", ", ".")
        res.add(maskStrBin)
        res.add(netwStrBin)
        return res
    }
}
