package voc.cn.cnvoccoin.util

/**
 * Created by shy on 2018/3/25.
 */
object UrlConstants{
    val RANK_URL:String = "/api/portal/voc/rank"//获取排行榜
    val MY_RANK_URL:String = "/api/portal/voc/my"//我的排行榜
    val REGISTER:String = "/api/user/public/register"//注册
    val LOGIN:String = "/api/user/public/login"//登录
    val GET_VOICE_TEXT = "/api/portal/corpus/getList"//获取语料文本
    val VERSION_CHECK = "/api/portal/app/check"//版本升级
    val UPLOAD_COIN = "/api/portal/voc/uploadVocCoin"//上传币
}
