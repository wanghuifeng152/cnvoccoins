package voc.cn.cnvoccoin.entity

import android.os.Parcel
import android.os.Parcelable
import android.support.annotation.Keep

/**
 * Created by shy on 2018/3/25.
 */
/*{
    "code": 1,
    "msg": "请求成功!",
    "data": [{
    "id": 1,
    "user_type": 1,
    "sex": 0,
    "birthday": 0,
    "last_login_time": 1521896883,
    "score": 0,
    "coin": 0,
    "balance": "0.00",
    "create_time": 1521896866,
    "user_status": 1,
    "user_login": "admin",
    "user_pass": "###9b0b59d370d847c79204bfc154424e93",
    "user_nickname": "admin",
    "user_email": "1@qq.com",
    "user_url": "",
    "avatar": "",
    "signature": "",
    "last_login_ip": "124.200.101.238",
    "user_activation_key": "",
    "mobile": "",
    "more": null,
    "voc_coin": 0
}, {
    "id": 2,
    "user_type": 2,
    "sex": 0,
    "birthday": 0,
    "last_login_time": 0,
    "score": 0,
    "coin": 0,
    "balance": "0.00",
    "create_time": 0,
    "user_status": 1,
    "user_login": "star",
    "user_pass": "",
    "user_nickname": "",
    "user_email": "",
    "user_url": "",
    "avatar": "",
    "signature": "",
    "last_login_ip": "",
    "user_activation_key": "",
    "mobile": "",
    "more": null,
    "voc_coin": 105
}]
}*/

@Keep
data class RankResModel(var code: Int, var msg: String, var data: List<DataBean>) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.createTypedArrayList(DataBean)) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(code)
        parcel.writeString(msg)
        parcel.writeTypedList(data)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RankResModel> {
        override fun createFromParcel(parcel: Parcel): RankResModel {
            return RankResModel(parcel)
        }

        override fun newArray(size: Int): Array<RankResModel?> {
            return arrayOfNulls(size)
        }
    }
}

@Keep
data class DataBean(var id: Int, var user_type: Int, var sex: Int, var birthday: Int, var last_login_time: Int, var score: Int,
                    var coin: Int, var balance: String, var create_time: Int, var user_status: Int, var user_login: String, var user_pass: String,var user_nickname:String, var user_email:String,var user_url:String,var avatar:String,var signature:String,var last_login_ip:String,var user_activation_key:String,var mobile:String,var more:String,var voc_coin:String) : Parcelable{
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeInt(user_type)
        parcel.writeInt(sex)
        parcel.writeInt(birthday)
        parcel.writeInt(last_login_time)
        parcel.writeInt(score)
        parcel.writeInt(coin)
        parcel.writeString(balance)
        parcel.writeInt(create_time)
        parcel.writeInt(user_status)
        parcel.writeString(user_login)
        parcel.writeString(user_pass)
        parcel.writeString(user_nickname)
        parcel.writeString(user_email)
        parcel.writeString(user_url)
        parcel.writeString(avatar)
        parcel.writeString(signature)
        parcel.writeString(last_login_ip)
        parcel.writeString(user_activation_key)
        parcel.writeString(mobile)
        parcel.writeString(more)
        parcel.writeString(voc_coin)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DataBean> {
        override fun createFromParcel(parcel: Parcel): DataBean {
            return DataBean(parcel)
        }

        override fun newArray(size: Int): Array<DataBean?> {
            return arrayOfNulls(size)
        }
    }


}

