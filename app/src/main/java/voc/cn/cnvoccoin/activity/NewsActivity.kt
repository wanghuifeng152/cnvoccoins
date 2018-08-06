package voc.cn.cnvoccoin.activity

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.TypedValue
import kotlinx.android.synthetic.main.activity_news.*
import voc.cn.cnvoccoin.R


/**
 * Created by shy on 2018/5/14.
 */
const val TAG = "TAG"
class NewsActivity :BaseActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)
        iv_back.setOnClickListener { finish()}
        loadImg()
    }

    private fun loadImg() {
        val dm = resources.displayMetrics
        val screenWidth = dm.widthPixels
        val imageWidth = px2dp(screenWidth - 40)
        val imageHeight = px2dp(283)
        img1.setImageBitmap(compressBitmap(R.mipmap.chart, imageWidth, imageHeight))
        img2.setImageBitmap(compressBitmap(R.mipmap.chart2, imageWidth, imageHeight))
    }

    private fun px2dp(pxValue: Int): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pxValue.toFloat(), resources
                .displayMetrics).toInt()
    }

    private fun compressBitmap(bitmapId: Int, imageWidth: Int, imageHeight: Int): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        var bitmap = BitmapFactory.decodeResource(resources, bitmapId, options)

        val outWidth = options.outWidth
        val outHeight = options.outHeight

        var sampleSize = 1
        while (outWidth / sampleSize > imageWidth || outHeight / sampleSize > imageHeight) {
            sampleSize *= 3
        }
        options.inSampleSize = sampleSize
        options.inJustDecodeBounds = false
        bitmap = BitmapFactory.decodeResource(resources, bitmapId, options)
        return bitmap
    }
}