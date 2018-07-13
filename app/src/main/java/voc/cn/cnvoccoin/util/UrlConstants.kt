package voc.cn.cnvoccoin.util

/**
 * Created by shy on 2018/3/25.
 */
const val RANK_URL: String = "/api/portal/voc/rank"//获取排行榜
const val MY_RANK_URL: String = "/api/portal/voc/my"//我的排行榜
const val URL_REGISTER: String = "/api/user/public/register"//注册
const val URL_LOGIN: String = "/api/user/public/login"//登录
const val SMS_URL_LOGIN: String = "/api/user/Sms_code/SmsCode"//验证码登录
const val GET_VOICE_TEXT = "/api/portal/corpus/getList"//获取语料文本
const val VERSION_CHECK = "/api/portal/app/check"//版本升级
const val UPLOAD_COIN = "/api/portal/voc/uploadVocCoinV2"//上传币
const val MAKE_TASK = "/api/portal/voc/confirmCode"//确认任务
const val TODAY_RANK = "/api/portal/voc/getTop"//
const val MOBILE_CONFIRM_CODE = "/api/user/public/sendCode"//获取手机验证码
const val SHEQU_PICS = "/api/user/public/getCodeArr"//社区图片
const val SECCE_SS = "/api/user/User_money/bingAddress"//是否添加地址
const val ADD = "/api/user/User_money/moneyAddress"  //保存地址接口
const val GET_MESSAGE_CODE = "/api/user/public/smsendCode"//获取验证码
const val POST_MESSAGE_CODE = "/api/user/public/loginVerification"//验证验证码
const val POST_PASSWORD = "/api/user/Verification_code/chargeVoc"//提现voc到钱包地址
const val SHOU_ZHI_MING_XI = "/api/user/Verification_code/payments"//收支明细
const val ADDRES = "/api/user/Verification_code/getAddress"//添加地址
const val ZI_CHAN = "/api/user/Verification_code/putVoc"//我的资产
const val POST_IS_HAVE_PWD = "/api/user/Verification_code/seTransaction"//获取用户是否设置过密码
const val POST_PAY_PWD = "/api/user/Verification_code/PutForward"//设置支付密码
const val POST_RESET_PWD = "/api/user/Verification_code/resPass"//重置密码
const val POST_RESET_THREE = "/api/user/Verification_code/verifiPass"//支付密码
const val POST_NUMBER = "/api/user/Verification_code/standardes"//提币下限
const val GET_TASK =  "/api/user/public/taskstatus"//任务完成状态
const val POST_CHARGE =  "/api/user/Verification_code/charge"//任务完成状态




