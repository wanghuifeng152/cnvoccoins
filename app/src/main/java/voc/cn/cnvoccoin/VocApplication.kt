package voc.cn.cnvoccoin

import android.app.Application

/**
 * Created by shy on 2018/3/24.
 */
class VocApplication : Application {
    private val TAG = this.javaClass.simpleName


    constructor() {
        sInstance = this
    }

    companion object {
        private lateinit var sInstance: VocApplication
        fun getInstance(): VocApplication {
            return sInstance
        }
    }

}