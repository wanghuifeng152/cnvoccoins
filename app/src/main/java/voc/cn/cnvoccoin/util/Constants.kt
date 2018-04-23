package voc.cn.cnvoccoin.util

import android.support.annotation.Keep
import java.io.Serializable

/**
 * Created by shy on 2018/3/27.
 */
const val COIN_NUMBER = "COIN_NUMBER"
const val USER_ID = "USER_ID"
const val USER_NAME = "USER_MOBILE"
const val PASSWORD = "USER_EMAIL"
const val TOKEN = "TOKEN"
const val VOICE_TEXT = "VOICE_TEXT"
const val VOICE_TEXT_POPSITION = "VOICE_TEXT_POPSITION"
//------------------eventbus-----------------------------//
class LoginEvent(var flag:Int,var number:String)
@Keep
data class RegisterRequest(var username:String,var password:String,var invite_code:String):Serializable
@Keep
data class LoginRequest(var username:String,var password:String,var device_type:String):Serializable
@Keep
data class LoginResponse(var token:String,var user:UserBean):Serializable
/**
 * 			"id": 21,
"user_type": 2,
"sex": 0,
"birthday": 0,
"last_login_time": 0,
"score": 0,
"coin": 0,
"balance": "0.00",
"create_time": 1522323550,
"user_status": 1,
"user_login": "",
"user_pass": "###ce29ba4e90b76fe34f7e1344aad6cf7f",
"user_nickname": "",
"user_email": "",
"user_url": "",
"avatar": "",
"signature": "",
"last_login_ip": "",
"user_activation_key": "",
"mobile": "15726948412",
"more": null,
"voc_coin": "0.00"
 */
@Keep
data class UserBean(var id:Int,var user_type:Int,var sex:Int,var birthday:Int,var last_login_time:Int,var score:Int,var coin:Int,var balance:String,var create_time:Double,var user_status:Int,var user_login:String,var user_pass:String,var user_nickname:String,var user_email:String,var user_url:String,var avatar:String,var signature:String,var last_login_ip:String,var user_activation_key:String,var mobile:String,var more:String,var voc_coin:String):Serializable


@Keep
data class VersionResponse(var isUpdate:Int,var platform:String,var version:String,var url:String,var content:String):Serializable

@Keep
data class UploadCoinRequest(var userID:Int,var voc_coin:String)