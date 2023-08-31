package com.simon.bookofmagic.other

import android.util.Log
import io.michaelrocks.paranoid.Obfuscate

@Obfuscate
class Params {
    companion object{
        var campaignID = "none"
        var advID = ""
        val packageName = "com.simon.bookofmagic"
        var deepLink: Boolean = false
        val myCustomValue = "0or0g0a0n0i0c"
        var utm_source = myCustomValue
        var uid = "none"
        var uuid = ""
        val params = mapOf(
            1 to "jn53AJruhS",
            2 to "dyslegff",
            3 to "kM2myuz",
            4 to "Lh89Lh34",
            5 to "fz6DVk",
            6 to "CzCoP5",
            7 to "ubmnzA",
            8 to "h6YptY",
            9 to "zDEngl",
            10 to "y074",
        )
        var campaign: LinkedHashMap<String, String> = linkedMapOf(
            "${params[1]}" to myCustomValue,
            "${params[2]}" to myCustomValue,
            "${params[3]}" to myCustomValue,
            "${params[4]}" to myCustomValue,
            "${params[5]}" to myCustomValue,
        )
        var startLink=""
        const val APS_F_ID = "gCHbdEFJg3mZnm7MP5az89"
        fun logis(s:String){
            Log.d("OlympusFirsTest",s)
        }
    }


}