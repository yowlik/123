package com.simon.bookofmagic.other

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.orhanobut.hawk.Hawk
import com.simon.bookofmagic.R
import com.simon.bookofmagic.databinding.FragmentMainBinding


class MainFragment : Fragment() {


    lateinit var binding:FragmentMainBinding
    var count=0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentMainBinding.inflate(layoutInflater,container,false)
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.hero.setOnClickListener {
            count++
            binding.counter.text=count.toString()
            when(count){
                100->binding.hero.setBackgroundResource(R.drawable.thirdchara)
                1000->binding.hero.setBackgroundResource(R.drawable.secchara)
                10000->binding.hero.setBackgroundResource(R.drawable.firstchara)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    //back Block
                }
            })
        binding.policy.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_backFragment)
        }
    }


}