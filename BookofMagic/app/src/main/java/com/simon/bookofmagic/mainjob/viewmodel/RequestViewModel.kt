package com.simon.bookofmagic.mainjob.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simon.bookofmagic.other.Params.Companion.logis
import io.michaelrocks.paranoid.Obfuscate
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
@Obfuscate
class RequestViewModel : ViewModel() {
    private val _pasteLiveData= MutableLiveData<String>()
    val pasteLiveData: LiveData<String>
        get()=_pasteLiveData

    fun main() {
        viewModelScope.launch {
            val url = "https://pastebin.com/raw/cHqFQ94V"
            val client = OkHttpClient()

            val request = Request.Builder()
                .url(url)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    logis(e.toString())
                    _pasteLiveData.postValue("error")
                }

                override fun onResponse(call: Call, response: Response) {
                    if (!response.isSuccessful) {
                        _pasteLiveData.postValue("error")
                    }
                    val responseBody = response.body?.string() ?: ""
                    _pasteLiveData.postValue(parseResponse(responseBody))
                }
            })
        }
    }

    fun parseResponse(responseBody: String): String {
        var value=""
        val parts=responseBody.split(" ")
        logis(parts.toString())
        if(parts[2]==parts[4]){
            value="https://${parts[3]}${parts[0]}${parts[6]}${parts[5]}${parts[1]}?"
        }
        else{
            value="error"
        }
        logis(value)
        return value
    }
}