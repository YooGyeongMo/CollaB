package com.gmlab.team_benew.main.mypage

import com.google.gson.annotations.SerializedName

data class putMypageEmailData(
    @SerializedName("email")
    val email: String
){
    override fun toString() : String{
        return "email=$email"
    }
}
