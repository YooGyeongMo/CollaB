package com.gmlab.team_benew.main

import okhttp3.ResponseBody

interface MainLiveAlarmsView {
    //실시간 폴링 성공
    fun onMainLiveAlarmsGetSuccess(responseBody: ResponseBody)
    //실시간 폴링 실패
    fun onMainLiveAlarmsGetFailure()
}