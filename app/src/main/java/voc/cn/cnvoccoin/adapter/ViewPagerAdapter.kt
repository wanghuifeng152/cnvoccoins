package voc.cn.cnvoccoin.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 * Created by shy on 2018/3/24.
 */
class ViewPagerAdapter(supportFragmentManager: FragmentManager,var fragmentLists:ArrayList<Fragment> ) : FragmentPagerAdapter(supportFragmentManager) {

    override fun getItem(position: Int): Fragment {
        return fragmentLists[position]
    }

    override fun getCount(): Int {
        return fragmentLists.size
    }
}