package com.simon.bookofmagic.other

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.onesignal.OneSignal
import com.orhanobut.hawk.Hawk
import com.simon.bookofmagic.R
import com.simon.bookofmagic.mainjob.settings.InternetViewModel
import com.simon.bookofmagic.other.Params.Companion.logis
import io.michaelrocks.paranoid.Obfuscate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Obfuscate
class BackFragment: Fragment() {

    lateinit var intsModel: InternetViewModel
    private lateinit var webView: WebView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
        webView = WebView(requireContext())
        webView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        val rootView = FrameLayout(requireContext())
        rootView.addView(webView)
        return rootView
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBackPressed()
        webSettins(webView)
        webClient(webView)
        webChrome(webView)
        checkInet()
        lifecycleScope.launch(Dispatchers.Main) { OneSignal.promptForPushNotifications(true) }

    }
    private fun checkInet() {
        intsModel = ViewModelProvider(this)[InternetViewModel::class.java]
        intsModel.isConnected.observe(requireActivity(), Observer { isConnected ->
            if (isConnected) {
                val open = Hawk.get("NUMS", "WEPOLICY")
                if (open == "POLICY") {
                    webView.loadUrl("https://bookofmagic.fun/")
                } else {
                    val lastVisitedLink = getLastVisitedLink()
                    if (lastVisitedLink != null) {
                        webView.loadUrl(lastVisitedLink)
                    } else {
                        val urlFirst = Hawk.get("Link", "https://bookofmagic.fun/")
                        logis(urlFirst)
                        webView.loadUrl(urlFirst)
                    }
                }
            } else {
                findNavController().navigate(R.id.action_backFragment_to_workFragment)
            }
        })
        intsModel.registerNetworkCallback()

    }




    override fun onDestroyView() {
        super.onDestroyView()
        intsModel.unregisterNetworkCallback()
    }


    var filePath: ValueCallback<Array<Uri>>? = null
    val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                filePath?.onReceiveValue(
                    WebChromeClient.FileChooserParams.parseResult(result.resultCode, result.data)
                )

                filePath = null

            } else if (result.resultCode == Activity.RESULT_CANCELED) {
                filePath?.onReceiveValue(null)
            }
        }

    private fun webChrome(webView: WebView) {
        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                if (newProgress == 100) {
                    saveLastVisitedLink(webView.url)
                }
            }

            override fun onShowFileChooser(
                webView: WebView?,
                filePathCallback: ValueCallback<Array<Uri>>?,
                fileChooserParams: FileChooserParams?
            ): Boolean {
                this@BackFragment.filePath = filePathCallback

                val contentIntent = Intent(Intent.ACTION_GET_CONTENT)
                contentIntent.type = "*/*"
                contentIntent.addCategory(Intent.CATEGORY_OPENABLE)

                this@BackFragment.resultLauncher.launch(contentIntent)
                return true
            }
        }
    }

    @Suppress("DEPRECATION")
    @SuppressLint("SetJavaScriptEnabled")
    fun webSettins(webView: WebView) {
        webView.settings.javaScriptEnabled = true
        webView.settings.useWideViewPort = true
        webView.settings.loadWithOverviewMode = true
        webView.settings.allowFileAccess = true
        webView.settings.domStorageEnabled = true
        webView.settings.userAgentString = "Mozilla/5.0 (Linux; Android 11; Pixel 5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.104 Mobile Safari/537.36"
        webView.settings.javaScriptCanOpenWindowsAutomatically = true
        webView.settings.setSupportMultipleWindows(false)
        webView.settings.displayZoomControls = false
        webView.settings.builtInZoomControls = true
        webView.settings.setSupportZoom(true)
        webView.settings.pluginState = WebSettings.PluginState.ON
        webView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        webView.settings.allowContentAccess = true
        val cookieManager = CookieManager.getInstance()
        cookieManager.setAcceptCookie(true)
        cookieManager.setAcceptThirdPartyCookies(webView, true)
    }

    private fun webClient(webView: WebView) {
        val activity: Activity = requireActivity()
        webView.webViewClient = object : WebViewClient() {
            @Deprecated("Deprecated in Java")
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                if (url.startsWith("https://www.instagram.com/") || url.startsWith("https://www.whatsapp.com/") ||
                    url.startsWith("https://t.me/") || url.contains("viber.com") || url.startsWith("tg:")
                ) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    startActivity(intent)
                    return true
                }
                return false
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
            }

            @Deprecated(
                "Deprecated in Java", ReplaceWith(
                    "Toast.makeText(activity, description, Toast.LENGTH_SHORT).show()",
                    "android.widget.Toast",
                    "android.widget.Toast"
                )
            )
            override fun onReceivedError(
                view: WebView,
                errorCode: Int,
                description: String,
                failingUrl: String
            ) {
                Toast.makeText(activity, description, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getLastVisitedLink(): String? {
        val sharedPreferences = context?.getSharedPreferences("Fragment", Context.MODE_PRIVATE)
        return sharedPreferences?.getString("lastVisitedLink", null)
    }

    private fun saveLastVisitedLink(link: String?) {
        val sharedPreferences = context?.getSharedPreferences("Fragment", Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        editor?.putString("lastVisitedLink", link)
        editor?.apply()
    }




    private fun initBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val open = Hawk.get("NUMS", "WEPOLICY")
                    when (open) {
                        "POLICY" -> {
                            findNavController().navigate(R.id.action_backFragment_to_mainFragment)

                        }
                        "WEPOLICY"-> {
                            if (webView.canGoBack()) {
                                webView.goBack()
                            }
                        }
                    }
                }
            })
    }


}