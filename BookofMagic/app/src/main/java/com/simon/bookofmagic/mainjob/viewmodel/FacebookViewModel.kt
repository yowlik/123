package com.simon.bookofmagic.mainjob.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.facebook.applinks.AppLinkData
import com.simon.bookofmagic.other.Params.Companion.campaign
import com.simon.bookofmagic.other.Params.Companion.deepLink
import com.simon.bookofmagic.other.Params.Companion.logis
import com.simon.bookofmagic.other.Params.Companion.params
import io.michaelrocks.paranoid.Obfuscate

@Obfuscate
class FacebookViewModel: ViewModel() {
    private val _facebookLiveData= MutableLiveData<String>()
    val facebookLiveData: LiveData<String>
        get()=_facebookLiveData


    fun facebookStart(context:Context) {
        AppLinkData.fetchDeferredAppLinkData(context) { applinkData ->
            logis("Face : ${applinkData.toString()}")
            if (applinkData != null) {
                val deeplink = applinkData.targetUri?.authority.toString()
                deepLink = true
                val facebookparams = deeplink.split("_").toTypedArray()
                if (facebookparams.isNotEmpty() && !facebookparams.contains("organic")) {
                    campaign.clear()
                    for (a in facebookparams.indices) {
                        val t = a + 1
                        campaign[params[t]!!] = facebookparams[a]
                    }
                }
            }
            _facebookLiveData.postValue("StartGen")
        }
    }
}