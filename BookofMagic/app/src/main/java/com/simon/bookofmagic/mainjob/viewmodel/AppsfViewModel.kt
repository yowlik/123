package com.simon.bookofmagic.mainjob.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import com.simon.bookofmagic.other.Params.Companion.APS_F_ID
import com.simon.bookofmagic.other.Params.Companion.campaign
import com.simon.bookofmagic.other.Params.Companion.campaignID
import com.simon.bookofmagic.other.Params.Companion.myCustomValue
import com.simon.bookofmagic.other.Params.Companion.params
import com.simon.bookofmagic.other.Params.Companion.uid
import com.simon.bookofmagic.other.Params.Companion.utm_source
import io.michaelrocks.paranoid.Obfuscate
import kotlinx.coroutines.launch
@Obfuscate
class AppsfViewModel : ViewModel(){
    private val _appsLiveData= MutableLiveData<String>()
    val appsLiveData: LiveData<String>
        get()=_appsLiveData

    fun getApps(cont: Context){
        viewModelScope.launch {
            AppsFlyerLib.getInstance()
                .init(
                    APS_F_ID,
                    object : AppsFlyerConversionListener {
                        override fun onConversionDataSuccess(data: MutableMap<String, Any>?) {

                            parseApp(data)

                        }

                        override fun onConversionDataFail(p0: String?) {
                            _appsLiveData.postValue("organic")
                        }

                        override fun onAppOpenAttribution(p0: MutableMap<String, String>?) {
                        }

                        override fun onAttributionFailure(p0: String?) {
                        }
                    },
                    cont
                )
            AppsFlyerLib.getInstance().start(cont, APS_F_ID)
            uid = AppsFlyerLib.getInstance().getAppsFlyerUID(cont) ?: "none"
        }
    }
    fun parseApp(result : MutableMap<String,Any>?){
        if(result==null){
            _appsLiveData.postValue("organic")
        }
        else{
            Log.d("TestLogsF","Else")
            if (result.containsKey("campaign_id")) campaignID = result["campaign_id"].toString()

            if (result.containsKey("media_source") && result["media_source"].toString()
                    .isNotEmpty() && result["media_source"].toString() !in arrayOf("none", "null")
            ) {
                utm_source = result["media_source"].toString()
                    .run { if (this == "organic") myCustomValue else this }
            }

            if (result.containsKey("campaign")) {
                result["campaign"].toString().split("_").let {
                    if (it.isNotEmpty() && (((it.size == 1) && it[0].lowercase() !in arrayOf(
                            "none",
                            "null"
                        )) || (it.size > 1))
                    ) {
                        for (item in it.indices) {
                            campaign["${params[item + 1]}"] =
                                it[item].run { if (this == "organic") myCustomValue else this }
                        }
                    }
                }
            }
            if (campaign.containsValue(myCustomValue) ||
                campaign.contains("none") || campaign.contains("null")
            ){
                _appsLiveData.postValue("organic")
            }
            else{
                _appsLiveData.postValue("nonorganic")
            }

        }
    }
}