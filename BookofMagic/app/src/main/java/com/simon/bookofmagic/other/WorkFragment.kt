package com.simon.bookofmagic.other

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.orhanobut.hawk.Hawk
import com.simon.bookofmagic.R
import com.simon.bookofmagic.databinding.FragmentWorkBinding
import com.simon.bookofmagic.mainjob.settings.InternetViewModel
import com.simon.bookofmagic.mainjob.viewmodel.AppsfViewModel
import com.simon.bookofmagic.mainjob.viewmodel.FacebookViewModel
import com.simon.bookofmagic.mainjob.viewmodel.RequestViewModel
import com.simon.bookofmagic.other.Params.Companion.advID
import com.simon.bookofmagic.other.Params.Companion.campaign
import com.simon.bookofmagic.other.Params.Companion.campaignID
import com.simon.bookofmagic.other.Params.Companion.deepLink
import com.simon.bookofmagic.other.Params.Companion.logis
import com.simon.bookofmagic.other.Params.Companion.packageName
import com.simon.bookofmagic.other.Params.Companion.startLink
import com.simon.bookofmagic.other.Params.Companion.uid
import com.simon.bookofmagic.other.Params.Companion.utm_source
import com.simon.bookofmagic.other.Params.Companion.uuid


class WorkFragment : Fragment() {


    private val appsModel : AppsfViewModel by activityViewModels()
    private val facebookModel : FacebookViewModel by activityViewModels()
    private val requestModel : RequestViewModel by activityViewModels()
    private lateinit var binding:FragmentWorkBinding
    private lateinit var goInt:InternetViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        binding= FragmentWorkBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkInet()
        waitRequest()
        waitData()
        waitFace()
    }

    private fun checkInet() {
        goInt = ViewModelProvider(this)[InternetViewModel::class.java]
        goInt.isConnected.observe(requireActivity(), Observer { isConnected ->
            if (isConnected) {
                binding.loader.setAnimation(R.raw.lottie)
                when(Hawk.get("NUMS","FIRST")){
                    "FIRST"->requestModel.main()
                    "POLICY"->findNavController().navigate(R.id.action_workFragment_to_mainFragment)
                    "WEPOLICY"->findNavController().navigate(R.id.action_workFragment_to_backFragment)
                }
            } else {
                binding.loader.setAnimation(R.raw.intes)
            }
        })
        goInt.registerNetworkCallback()
    }

    private fun waitRequest(){
        requestModel.pasteLiveData.observe(viewLifecycleOwner){
            if(it=="error"){
                Hawk.put("NUMS","POLICY")
                findNavController().navigate(R.id.action_workFragment_to_mainFragment)
            }
            else{
                startLink=it
                appsModel.getApps(requireContext())
            }

        }
    }

    private fun waitData(){
        appsModel.appsLiveData.observe(viewLifecycleOwner){
            if(it=="organic"){
                facebookModel.facebookStart(requireContext())
            }
            else{
                generate()
            }
        }
    }

    private fun waitFace(){
        facebookModel.facebookLiveData.observe(viewLifecycleOwner){
            if(it=="StartGen"){
                generate()
            }
        }
    }

    private fun generate(){
        for((key,value) in campaign){
            startLink="$startLink$key=$value&"
        }
        logis("${startLink}Djltdzt=$packageName&h67UD=$utm_source&0BOnUgS6G=$uid&45culdox=$uuid&v79ddur=$advID&emrstz=$campaignID&QLVuqRWjo=$deepLink")
        Hawk.put("Link","${startLink}Djltdzt=$packageName&h67UD=$utm_source&0BOnUgS6G=$uid&45culdox=$uuid&v79ddur=$advID&emrstz=$campaignID&QLVuqRWjo=$deepLink")
        Hawk.put("NUMS","WEPOLICY")
        findNavController().navigate(R.id.action_workFragment_to_backFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        goInt.unregisterNetworkCallback()
    }


}