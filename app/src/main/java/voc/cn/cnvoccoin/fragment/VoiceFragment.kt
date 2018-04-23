package voc.cn.cnvoccoin.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_voice.*
import voc.cn.cnvoccoin.R

/**
 * Created by shy on 2018/3/24.
 */
class VoiceFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_voice, null)
        return view
    }

    override fun onResume() {
        super.onResume()

        rsv_voice.setOnTouchListener { v, event ->
            when(event.action){
                MotionEvent.ACTION_DOWN->{
                    rsv_voice.setAnimation(true)
                    true
                }
                MotionEvent.ACTION_UP->{
                    rsv_voice.setAnimation(false)
                    true
                }
            }
            true

        }
    }

}