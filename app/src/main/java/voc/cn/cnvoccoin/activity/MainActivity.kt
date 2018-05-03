package voc.cn.cnvoccoin.activity

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.support.v4.view.ViewPager
import android.view.KeyEvent
import android.view.MotionEvent
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.json.JSONObject
import voc.cn.cnvoccoin.R
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
    var voiceLists: List<VoiceTextBean.DataBean> = arrayListOf()
    var position: Int = 0
    var permissinTag = true
    lateinit var registerDialog: LoginDialog
    var random = arrayOf(0.13,0.23,0.33)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
//        requestWindowFeature(Window.FEATURE_NO_TITLE)
//        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)
        initViewPager()
        initRadioButton()
        requestPermission()
        checkVersion()
//        showRegisterDialog()
//        initView()
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
                Toast.makeText(this@MainActivity, "正在下载请稍后", Toast.LENGTH_SHORT).show()
                true
            } else {
                false
            }
        }
        // Sdcard不可用
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this@MainActivity, "SD卡不可用~", Toast.LENGTH_SHORT).show()
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
        var intent = Intent()
        // 执行动作
        intent.action = Intent.ACTION_VIEW
        // 执行的数据类型
        var uri: Uri? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            FileProvider.getUriForFile(this, "voc.cn.cnvoccoin.fileprovider", file)
        } else {
            Uri.fromFile(file)
        }
        intent.setDataAndType(uri, "application/vnd.android.package-archive")
        intent.flags = Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun getVoiceList() {
        voiceLists = PreferenceUtil.instance?.get<List<VoiceTextBean.DataBean>>(VOICE_TEXT, object : TypeToken<List<VoiceTextBean.DataBean>>() {}.type) ?: arrayListOf()
        position = PreferenceUtil.instance?.getInt(VOICE_TEXT_POPSITION) ?: 0
        if (voiceLists.isNotEmpty()) return
        HttpManager.get(GET_VOICE_TEXT).subscribe(object : Subscriber<String> {
            override fun onNext(s: String?) {
                if (s == null || s.isEmpty()) return
                val gson = Gson()
                val bean = gson.fromJson(s, VoiceTextBean::class.java) ?: return
                if (bean.code == 1 && bean.data != null) {
                    val dataList = bean.data
                    PreferenceUtil.instance?.set(VOICE_TEXT, dataList)
                    voiceLists = dataList
                }


            }

            override fun onError(t: Throwable?) {
            }

            override fun onComplete() {
            }

        })
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
            val buffer = ByteArray(1024)
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

    private fun showRegisterDialog() {
        val mobile = PreferenceUtil.instance?.getString(USER_NAME)
        val password = PreferenceUtil.instance?.getString(PASSWORD)
        val token = PreferenceUtil.instance?.getString(TOKEN)


        if (mobile == null || mobile.isEmpty() || password == null || password.isEmpty() || token == null || token.isEmpty()) {
            registerDialog = LoginDialog(this, REGISTER)
            registerDialog.show()
        }else{
            getLogin(false)
        }
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE),
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE_)
            }
        }
    }

/*    private fun initView() {

        iv_rank.setOnClickListener {
            startActivity(Intent(this@MainActivity, RankActivity::class.java))

        }

        iv_info.setOnClickListener {
            startActivity(Intent(this@MainActivity, InfoActivity::class.java))
        }

        iv_head.setOnClickListener {
            val username = PreferenceUtil.instance?.getString(USER_NAME)
            val password = PreferenceUtil.instance?.getString(PASSWORD)
            if (username != null && username.isNotEmpty() && password != null && password.isNotEmpty()) {
                getLogin(true)
            }
        }

        val position = PreferenceUtil.instance?.getInt(VOICE_TEXT_POPSITION, 0)?:0
        if(voiceLists.isEmpty())return
        tv_voice.text = voiceLists[position].content
    }*/

    private fun getLogin(showLoginDialog: Boolean) {
        val username = PreferenceUtil.instance?.getString(USER_NAME) ?: ""
        val password = PreferenceUtil.instance?.getString(PASSWORD) ?: ""
        if(username.isEmpty())return

        val loadingDialog = LoadingDialog(this, null)
        loadingDialog.show()

        val request = LoginRequest(username, password, "android")
        var wrapper: RequestBodyWrapper = RequestBodyWrapper(request)
        HttpManager.post(URL_LOGIN, wrapper).subscribe(object : Subscriber<ResBaseModel<LoginResponse>> {
            override fun onComplete() {
                loadingDialog.dismiss()
            }

            override fun onNext(model: ResBaseModel<LoginResponse>?) {
                if (model?.data == null) return
                PreferenceUtil.instance?.set(TOKEN, model.data.token)
                if (model?.data.user != null) {
                    PreferenceUtil.instance?.set(USER_ID, model.data.user.id)
                }

                if(showLoginDialog){
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

  /*  private fun setCoin(length: Int) {
        val userId = PreferenceUtil.instance?.getInt(USER_ID)
        voiceLists = PreferenceUtil.instance?.get<List<VoiceTextBean.DataBean>>(VOICE_TEXT, object : TypeToken<List<VoiceTextBean.DataBean>>() {}.type) ?: arrayListOf()
        position = PreferenceUtil.instance?.getInt(VOICE_TEXT_POPSITION) ?: 0
        if (userId == 0) return
        val random1 = Random()
        var s = random1.nextInt(3)
        val coin = BigDecimal(random[s]).multiply(BigDecimal(length)).setScale(2, BigDecimal.ROUND_HALF_UP)//保留两位小数
        val request = UploadCoinRequest(userId!!, coin.toString())
        var wrapper: RequestBodyWrapper = RequestBodyWrapper(request)
        HttpManager.post(UPLOAD_COIN, wrapper).subscribe(object : Subscriber<String> {
            override fun onNext(s: String?) {
                if (s == null) return
                val jsonObject = JSONObject(s)
                val code = jsonObject.getInt("code")
                if (code == 1) {
//                    tv_my_coin.text = BigDecimal( tv_my_coin.text.toString()).add(coin).toString()
                    val data = jsonObject.getJSONObject("data")
                    tv_my_coin.text = data.getString("voc_coin")

                    if(voiceLists.isEmpty())return
                    val size = voiceLists?.size ?: 0
                    position++
                    if (position < size) {
                        tv_voice.text = voiceLists?.get(position)?.content
//                        position++
                    } else {
                        tv_voice.text = voiceLists?.get(position - size)?.content
                        position -= size
                    }
                    PreferenceUtil.instance?.set(VOICE_TEXT_POPSITION, position)
                }


            }

            override fun onError(t: Throwable?) {
            }

            override fun onComplete() {
            }

        })
    }*/

    private fun touchTime(longTime: Int): Boolean {
        var lastClickTime: Long = 0

        val time = System.currentTimeMillis()
        val endTime = time - lastClickTime
        if (endTime in 1..(longTime - 1)) {
            return false
        }
        lastClickTime = time
        return true
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
                ToastUtil.showToast("权限已申请")
                permissinTag = true
            } else {
                ToastUtil.showToast("权限已拒绝")
                permissinTag = false
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }



    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}
