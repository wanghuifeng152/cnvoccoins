package voc.cn.cnvoccoin.activity

import voc.cn.cnvoccoin.R
import voc.cn.cnvoccoin.util.FOCUS_COMMON

/**
 * Created by shy on 2018/5/8.
 */
class FocusOfficalActivity:CommnutityActivity() {
    override fun getLayoutResId(): Int {
        return R.layout.activity_focus_offical
    }

    override fun setParams() {
        tag = FOCUS_COMMON
        imgId = R.mipmap.gongzhonghao.toString()

    }

}