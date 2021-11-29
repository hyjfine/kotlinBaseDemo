package com.dawn.kotlinbasedemo

import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Button
import com.dawn.kotlinbasedemo.databinding.ActivityMainBinding
import com.dawn.kotlinbasedemo.vm.MainVm
import com.dawn.lib_base.base.BaseActivity


class MainActivity : BaseActivity<ActivityMainBinding, MainVm>() {
    private val TAG = "MainActivity"
    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initData(savedInstanceState: Bundle?) {

        findViewById<Button>(R.id.click1).setOnClickListener {
            Log.i(TAG, "-----click ")
            getNetworkType()
        }
//        viewModel.requestData();
//        vm.requestFlowData()
//        viewModel.requestLoopData();
//        viewModel.requestWithTake()


    }

    override fun getVariableId(): Int {
        return BR.mainVm

    }


    private fun getNetworkType() {
        val telephonyManager = this.getSystemService(TELEPHONY_SERVICE) as TelephonyManager
        var type = -1
//        try {
        type = telephonyManager.dataNetworkType
//       val type =  telephonyManager.hasCarrierPrivileges()
//        TestTool.makeType(this)
//        } catch (e: Exception) {
//        }
        Log.i(TAG, "-----netType $type")
    }


}