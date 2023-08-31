package com.simon.bookofmagic.mainjob.app

import android.app.Application
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.onesignal.OneSignal
import com.orhanobut.hawk.Hawk
import com.simon.bookofmagic.other.Params.Companion.advID
import com.simon.bookofmagic.other.Params.Companion.uuid
import io.michaelrocks.paranoid.Obfuscate
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.UUID
@Obfuscate
class ApplicationClass : Application() {
    override fun onCreate() {
        super.onCreate()
        Hawk.init(this).build()
        getAdId()
        OneSignal()

    }

    private fun getAdId(){
        GlobalScope.launch {
            val adInfo = AdvertisingIdClient(applicationContext)
            adInfo.start()
            advID = adInfo.info.id.toString()
        }
    }

    private fun OneSignal(){
        uuid = Hawk.get("UUID", UUID.randomUUID().toString())
        Hawk.put("UUID", uuid)
        OneSignal.initWithContext(applicationContext)
        OneSignal.setAppId("5ff7ad9c-4bb5-437a-aae0-8ff86b3e40b1")
        OneSignal.setExternalUserId(uuid)
    }

}