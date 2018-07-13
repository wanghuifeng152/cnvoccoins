package voc.cn.cnvoccoin.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.app.NotificationCompat.*
import android.support.v4.content.ContextCompat
import android.support.v4.content.ContextCompat.startActivity
import android.support.v4.content.FileProvider
import android.support.v4.view.ViewPager
import android.telephony.TelephonyManager
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.widget.Toast
import cn.jpush.android.api.BasicPushNotificationBuilder
import cn.jpush.android.api.CustomPushNotificationBuilder
import cn.jpush.android.api.JPushInterface
import cn.jpush.android.api.MultiActionsNotificationBuilder
import cn.jpush.android.data.JPushLocalNotification
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.json.JSONObject
import voc.cn.cnvoccoin.R
import voc.cn.cnvoccoin.R.id.rg
import voc.cn.cnvoccoin.R.id.vp_coin
import voc.cn.cnvoccoin.activity.Constant.IS_GRANTED_PERMISSION
import voc.cn.cnvoccoin.adapter.ViewPagerAdapter
import voc.cn.cnvoccoin.dialog.LOGIN
import voc.cn.cnvoccoin.dialog.LoginDialog
import voc.cn.cnvoccoin.dialog.REGISTER
import voc.cn.cnvoccoin.entity.MyCoinResponse
import voc.cn.cnvoccoin.entity.VoiceTextBean
import voc.cn.cnvoccoin.fragment.HomeFragment
import voc.cn.cnvoccoin.fragment.UserFragment
import voc.cn.cnvoccoin.network.HttpManager
import voc.cn.cnvoccoin.network.RequestBodyWrapper
import voc.cn.cnvoccoin.network.ResBaseModel
import voc.cn.cnvoccoin.network.Subscriber
import voc.cn.cnvoccoin.util.*
import voc.cn.cnvoccoin.view.LoadingDialog
import voc.cn.cnvoccoin.view.YHShopDialog
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.math.BigDecimal
import java.net.URL
import java.util.*


const val MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE_ = 1

class MainActivity : BaseActivity() {
    var permissinTag = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
        setContentView(R.layout.activity_main)
        initViewPager()
        initRadioButton()
        requestPermission()
        checkVersion()
        /**
        *《---------------------------------------------==- 自定义极光 -==---------------------------------------------》
        */
//        自定义推送UI
        val builder2 = CustomPushNotificationBuilder(this@MainActivity,
                R.layout.customer_notitfication_layout,
                R.id.icon,
                R.id.title,
                R.id.text)
        // 指定定制的 Notification Layout
        builder2.statusBarDrawable = R.drawable.ic_launcher
        // 指定最顶层状态栏小图标
        builder2.layoutIconDrawable = R.drawable.ic_launcher
        // 指定下拉状态栏时显示的通知图标
        JPushInterface.setPushNotificationBuilder(1, builder2)
//        本地推送
//        val ln = JPushLocalNotification()
//        ln.builderId = 1
//        ln.content = "你好"
//        ln.title = "离线"
//        ln.notificationId = 11111111
//        ln.setBroadcastTime(2018,7,11,17,28,0)
//        val mBageMap = mutableMapOf("type" to "1")
//        val json = JSONObject(mBageMap)
//        ln.extras = json.toString()
//        JPushInterface.addLocalNotification(this@MainActivity, ln)
    }


    private fun checkVersion() {
        try {
            val map = hashMapOf<String, String>()
            map["platform"] = "android"
            HttpManager.get(VERSION_CHECK, map).subscribe(object : Subscriber<ResBaseModel<VersionResponse>> {
                override fun onNext(model: ResBaseModel<VersionResponse>?) {
                    if (model == null) return
                    if (model.code == 1 && model.data != null) {
                       when (model.data.isUpdate) {

                           0 -> return
                           1 -> showUpdataDialog(model.data, false)
                       2 -> showUpdataDialog(model.data, true)
                       }




                    }
                }

                override fun onError(t: Throwable?) {
                }

                override fun onComplete() {
                }

            }, VersionResponse::class.java, ResBaseModel::class.java)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }


    }

    private fun showUpdataDialog(model: VersionResponse, forceUpdata: Boolean) {
        val dialog = YHShopDialog(this, R.style.TradeDialog)
        dialog.setMessage(model.content)
        dialog.setOnCancelClick {
            if (forceUpdata) {
                finish()
            } else {
                dialog.dismiss()
            }
        }

        dialog.setOnComfirmClick {
            dialog.dismiss()
            downLoadApk(model)
        }
        dialog.show()
    }

    private fun downLoadApk(model: VersionResponse) {
        var pd = ProgressDialog(this)
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        pd.setMessage("下载中...")
        pd.setCanceledOnTouchOutside(false)
        pd.setCancelable(false)
        // 监听返回键--防止下载的时候点击返回
        pd.setOnKeyListener { dialog, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.repeatCount === 0) {
//                Toast.makeText(this@MainActivity, "正在下载请稍后", Toast.LENGTH_SHORT).show()
                ToastUtil.showToast("正在下载请稍后")
                true
            } else {
                false
            }
        }
        // Sdcard不可用
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//            Toast.makeText(this@MainActivity, "SD卡不可用~", Toast.LENGTH_SHORT).show()
            ToastUtil.showToast("SD卡不可用")
        } else {
            pd.show()
            //下载的子线程
            object : Thread() {
                override fun run() {
                    try {
                        // 在子线程中下载APK文件
                        var file: File = getFileFromServer(model.url, pd)
                                ?: return
                        Thread.sleep(1000)
                        // 安装APK文件
                        pd.dismiss() // 结束掉进度条对话框
                        installApk(file)
                    } catch (e: Exception) {
                        pd.dismiss()
                        e.printStackTrace()
                    }

                }
            }.start()
        }
    }


    fun installApk(file: File) {
//        var intent = Intent()
//        // 执行动作
//        intent.action = Intent.ACTION_VIEW
//        // 执行的数据类型
//        var uri: Uri? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            FileProvider.getUriForFile(this, "voc.cn.cnvoccoin.fileprovider", file)
//        } else {
//            Uri.fromFile(file)
//        }
        val uri = Uri.fromFile(file);
//        String[] command = { "chmod", "777", file.getPath() };
//        ProcessBuilder builder = new ProcessBuilder();
//        try {
//            builder.start();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        if (Build.VERSION.SDK_INT >= 24) {//判读版本是否在7.0以上
            val apkUri = FileProvider.getUriForFile(this@MainActivity, "voc.cn.cnvoccoin.fileprovider", file)
            val install = Intent(Intent.ACTION_VIEW)
//            install.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)//添加这一句表示对目标应用临时授权该Uri所代表的文件
            install.setDataAndType(apkUri, "application/vnd.android.package-archive")
            startActivity(install)
        } else {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(uri, "application/vnd.android.package-archive")
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)

        }

//        intent.setDataAndType(uri, "application/vnd.android.package-archive")
//        intent.flags = Intent.FLAG_GRANT_WRITE_URI_PERMISSION
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//        startActivity(intent)
    }

    fun getFileFromServer(path: String, pd: ProgressDialog): File? {
        // 如果相等的话表示当前的sdcard挂载在手机上并且是可用的
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            var url = URL(path)
            var conn = url.openConnection()
            conn.connectTimeout = 5000
            // 获取到文件的大小
            pd.max = conn.contentLength / 1024
            var ins = conn.getInputStream()

            var file = File(Environment.getExternalStorageDirectory().path
                    + "/voc", "VocalChain.apk")
            //判断文件夹是否被创建
            if (!file.parentFile.exists()) {
                file.parentFile.mkdirs()
            }
            var fos = FileOutputStream(file)
            var bis = BufferedInputStream(ins)
            val buffer = ByteArray(64)
            var len: Int = 0
            var total: Int = 0
            while (ins.read(buffer).apply { len = this } != -1) {
                fos.write(buffer, 0, len)
                total += len
                // 获取当前下载量
                pd.progress = total / 1024
            }
            fos.close()
            bis.close()
            ins.close()
            return file
        } else {
            return null
        }
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE),
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE_)
            }

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.RECORD_AUDIO),
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE_)
            }
        }
    }


    private fun getLogin(showLoginDialog: Boolean) {
        val username = PreferenceUtil.instance?.getString(USER_NAME) ?: ""
        val password = PreferenceUtil.instance?.getString(PASSWORD) ?: ""
        if (username.isEmpty() || password.isEmpty()) return

        val loadingDialog = LoadingDialog(this, null)
        loadingDialog.show()

        val request = LoginRequest(username, password, "android")
        var wrapper: RequestBodyWrapper = RequestBodyWrapper(request)
        HttpManager.post(URL_LOGIN, wrapper).subscribe(object : Subscriber<ResBaseModel<LoginResponse>> {
            override fun onComplete() {
                loadingDialog.dismiss()
            }

            override fun onNext(model: ResBaseModel<LoginResponse>?) {
                if (model?.data == null) {
                    PreferenceUtil.instance?.set(TOKEN, null)
                    return
                }


                PreferenceUtil.instance?.set(TOKEN, model.data.token)

                if (model?.data.user != null) {

                    PreferenceUtil.instance?.set(USER_ID, model.data.user.id)
                }

                if (showLoginDialog) {
                    val loginDialog = LoginDialog(this@MainActivity, LOGIN)
                    loginDialog.show()
                }


            }

            override fun onError(t: Throwable?) {
            }

        }, LoginResponse::class.java, ResBaseModel::class.java)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onNumberEvent(event: LoginEvent) {
        getLogin(true)
    }

    private fun initRadioButton() {
        rg.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioButton_rank -> vp_coin.currentItem = 0
                R.id.radioButton_voice -> vp_coin.currentItem = 1
                R.id.radioButton_user -> vp_coin.currentItem = 2

            }
        }
    }

    private fun initViewPager() {
        var fragmentLists: ArrayList<Fragment> = arrayListOf()
        fragmentLists.add(HomeFragment())
        fragmentLists.add(UserFragment())
        val adapter = ViewPagerAdapter(supportFragmentManager, fragmentLists)
        vp_coin.adapter = adapter

        vp_coin.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> rg.check(R.id.radioButton_rank)
                    1 -> rg.check(R.id.radioButton_voice)
                    2 -> rg.check(R.id.radioButton_user)
                }
            }

        })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {

        if (requestCode == MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE_) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                PreferenceUtil.instance?.set(IS_GRANTED_PERMISSION, true)
            } else {
                ToastUtil.showToast("权限已拒绝")
                PreferenceUtil.instance?.set(IS_GRANTED_PERMISSION, false)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }


    var exitTime : Long? = 0
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        ToastUtil.showToast("再按一次退出VOC")
        if(keyCode == KeyEvent.KEYCODE_BACK &&
                event!!.getAction() == KeyEvent.ACTION_DOWN){

            if((System.currentTimeMillis()- this!!.exitTime!!) >2000){
                ToastUtil.showToast("再按一次退出VOC")
                exitTime = System.currentTimeMillis()
            }else{
                finish()
            }

            return true
        }

        return super.onKeyDown(keyCode, event)
    }
}
