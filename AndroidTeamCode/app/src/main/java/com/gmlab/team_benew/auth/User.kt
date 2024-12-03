package com.gmlab.team_benew.auth

import com.google.gson.annotations.SerializedName
import java.io.Serial

data class User(
    @SerializedName(value = "account") var id : String,
    @SerializedName(value = "password") var password : String,
)

data class RegisterUser(
    @SerializedName(value = "account") var id : String,
    @SerializedName(value = "password") var password : String,
    @SerializedName(value = "name") var name : String,
    @SerializedName(value = "gender") var gender: String,
    @SerializedName(value = "birthday") var birthday: String,
    @SerializedName(value = "email") var email: String,
    @SerializedName(value = "major") var major: String,
    @SerializedName(value = "phoneNumber") var phoneNumber: String

)

data class AccountCheck(
    @SerializedName(value = "account") var id : String
)

data class EmailCheck(
    @SerializedName(value = "email") var email: String
)

data class PhoneCheck(
    @SerializedName(value = "phoneNumber") var phoneNumber: String
)