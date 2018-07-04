package voc.cn.cnvoccoin.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;
import voc.cn.cnvoccoin.R;
import voc.cn.cnvoccoin.entity.DetailedClass;
import voc.cn.cnvoccoin.network.HttpManager;
import voc.cn.cnvoccoin.network.RequestBodyWrapper;
import voc.cn.cnvoccoin.network.Subscriber;
import voc.cn.cnvoccoin.util.UrlConstantsKt;
import voc.cn.cnvoccoin.util.list;

;


public class DetailedActivity extends BaseActivity {


    @BindView(R.id.detailed_back)
    ImageView detailedBack;
    @BindView(R.id.yaoqing)
    TextView yaoqing;
    @BindView(R.id.tv_friends_time)
    TextView tvFriendsTime;
    @BindView(R.id.tv_friends_time_count)
    TextView tvFriendsTimeCount;
    @BindView(R.id.jiaru)
    TextView jiaru;
    @BindView(R.id.tv_community_shequ_time)
    TextView tvCommunityShequTime;
    @BindView(R.id.tv_community_shequ_time_count)
    TextView tvCommunityShequTimeCount;
    @BindView(R.id.gongzhong)
    TextView gongzhong;
    @BindView(R.id.number_guanzhu_time)
    TextView numberGuanzhuTime;
    @BindView(R.id.number_guanzhu_count)
    TextView numberGuanzhuCount;
    @BindView(R.id.xitong)
    TextView xitong;
    @BindView(R.id.number_xitong_time)
    TextView numberXitongTime;
    @BindView(R.id.number_xitong_count)
    TextView numberXitongCount;
    @BindView(R.id.tixian)
    TextView tixian;
    @BindView(R.id.tv_tixian_dao)
    TextView tvTixianDao;
    @BindView(R.id.tv_tixian_dao_time)
    TextView tvTixianDaoTime;
    @BindView(R.id.tv_tixian_dao_jine)
    TextView tvTixianDaoJine;
    @BindView(R.id.tv11)
    TextView tv11;
    @BindView(R.id.threevoc_santian_time1)
    TextView threevocSantianTime1;
    @BindView(R.id.threevoc_santian_count1)
    TextView threevocSantianCount1;
    @BindView(R.id.tv12)
    TextView tv12;
    @BindView(R.id.threevoc_santian_time2)
    TextView threevocSantianTime2;
    @BindView(R.id.threevoc_santian_count2)
    TextView threevocSantianCount2;
    @BindView(R.id.tv13)
    TextView tv13;
    @BindView(R.id.threevoc_santian_time3)
    TextView threevocSantianTime3;
    @BindView(R.id.threevoc_santian_count3)
    TextView threevocSantianCount3;
    @BindView(R.id.tv14)
    TextView tv14;
    @BindView(R.id.threevoc_santian_time4)
    TextView threevocSantianTime4;
    @BindView(R.id.threevoc_santian_count4)
    TextView threevocSantianCount4;
    @BindView(R.id.tv15)
    TextView tv15;
    @BindView(R.id.threevoc_santian_time5)
    TextView threevocSantianTime5;
    @BindView(R.id.threevoc_santian_count5)
    TextView threevocSantianCount5;
    @BindView(R.id.tv16)
    TextView tv16;
    @BindView(R.id.threevoc_santian_time6)
    TextView threevocSantianTime6;
    @BindView(R.id.threevoc_santian_count6)
    TextView threevocSantianCount6;
    @BindView(R.id.tv17)
    TextView tv17;
    @BindView(R.id.threevoc_santian_time7)
    TextView threevocSantianTime7;
    @BindView(R.id.threevoc_santian_count7)
    TextView threevocSantianCount7;
    @BindView(R.id.tv18)
    TextView tv18;
    @BindView(R.id.threevoc_santian_time8)
    TextView threevocSantianTime8;
    @BindView(R.id.threevoc_santian_count8)
    TextView threevocSantianCount8;
    @BindView(R.id.tv19)
    TextView tv19;
    @BindView(R.id.threevoc_santian_time9)
    TextView threevocSantianTime9;
    @BindView(R.id.threevoc_santian_count9)
    TextView threevocSantianCount9;
    @BindView(R.id.tv20)
    TextView tv20;
    @BindView(R.id.threevoc_santian_time10)
    TextView threevocSantianTime10;
    @BindView(R.id.threevoc_santian_count10)
    TextView threevocSantianCount10;
    @BindView(R.id.Mtixian)
    RelativeLayout recyclerView;
    @BindView(R.id.Mxitong)
    RelativeLayout Mxitong;
    @BindView(R.id.Myaoqing)
    RelativeLayout Myaoqing;
    @BindView(R.id.Mshequ)
    RelativeLayout Mshequ;
    @BindView(R.id.Mgongzhong)
    RelativeLayout Mgongzhong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
        ButterKnife.bind(this);
        initData();
        initListener();
    }

    private void initData() {
        list request = new list("1");
        RequestBodyWrapper wrapper = new RequestBodyWrapper(request);
        HttpManager.post(UrlConstantsKt.SHOU_ZHI_MING_XI, wrapper).subscribe(new Subscriber<String>() {

            @Override
            public void onNext(String s) {
                Log.e("TAG", "onNext: SSSSSSSSSSSSSSSSSS" + s);
                DetailedClass detailedClass = new Gson().fromJson(s, DetailedClass.class);
                DetailedClass.MsgBean msg = detailedClass.getMsg();
                //系统奖励
                DetailedClass.MsgBean._$4Bean msg_$4 = msg.get_$4();
                if (msg_$4 != null) {
                    tixian.setText(msg_$4.getTitle());
                    if (msg_$4.getAddress() == null) {
                        msg_$4.setAddress("");
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                    if (msg_$4.getSum() == null) {
                        msg_$4.setSum("");
                        recyclerView.setVisibility(View.GONE);

                    } else {
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                    if (msg_$4.getTime() == null) {
                        msg_$4.getTime();
                        recyclerView.setVisibility(View.GONE);

                    } else {
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                    tvTixianDao.setText(msg_$4.getAddress() + "");
                    tvTixianDaoJine.setText(msg_$4.getSum() + "");
                    tvTixianDaoTime.setText(msg_$4.getTime() + "");
                }
                //邀请好友
                DetailedClass.MsgBean._$0Bean msg_$0 = msg.get_$0();
                if (msg_$0 != null) {
                    xitong.setText(msg_$0.getTitle());
                    numberXitongTime.setText(msg_$0.getTime());
                    if (msg_$0.getSum() == null || "".equals(msg_$0.getSum())) {
                        msg_$0.setSum("");
                        Myaoqing.setVisibility(View.GONE);
                    }else{
                        Myaoqing.setVisibility(View.VISIBLE);
                    }
                    if (msg_$0.getTime() == null || "".equals(msg_$0.getTime())) {
                        msg_$0.setSum("");
                        Myaoqing.setVisibility(View.GONE);
                    }else{
                        Myaoqing.setVisibility(View.VISIBLE);
                    }
                    numberXitongCount.setText(msg_$0.getSum());
                }
                //加入社区
                DetailedClass.MsgBean._$1Bean msg_$1 = msg.get_$1();
                if (msg_$1 != null) {
                    if (msg_$1.getTitle() == null ||"".equals(msg_$1.getTitle())) {
                        msg_$1.setTitle("");
                        Mshequ.setVisibility(View.GONE);
                    }
                    if (msg_$1.getTime() == null || "0".equals(msg_$1.getTime())) {
                        msg_$1.setTime("");
                        Mshequ.setVisibility(View.GONE);
                    }else{
                        Mshequ.setVisibility(View.VISIBLE);
                    }
                    if (msg_$1.getSum() == null || "0".equals(msg_$1.getSum())) {
                        msg_$1.setSum("");
                        Mshequ.setVisibility(View.GONE);
                    }
                    yaoqing.setText(msg_$1.getTitle());
                    tvFriendsTime.setText(msg_$1.getTime());
                    tvFriendsTimeCount.setText(msg_$1.getSum());

                }
                //关注公众号
                DetailedClass.MsgBean._$2Bean msg_$2 = msg.get_$2();
                if (msg_$2 != null) {
                    jiaru.setText(msg_$2.getTitle());
                    tvCommunityShequTime.setText(msg_$2.getTime());
                    if (msg_$2.getSum() == null || "0".equals(msg_$2.getSum())) {
                        msg_$2.setSum("");
                        Mgongzhong.setVisibility(View.GONE);
                    }
                    if (msg_$2.getTime() == null || "".equals(msg_$2.getTime())) {
                        msg_$2.setTime("");
                        Mgongzhong.setVisibility(View.GONE);
                    }else{
                        Mgongzhong.setVisibility(View.VISIBLE);
                    }
                    tvCommunityShequTimeCount.setText(msg_$2.getSum());

                }
                //提现到
                DetailedClass.MsgBean._$3Bean msg_$3 = msg.get_$3();
                if (msg_$3 != null) {
                    if (msg_$3.getTitle() == null) {
                        msg_$3.setTitle("");
                        Mxitong.setVisibility(View.GONE);
                    }else{
                        Mxitong.setVisibility(View.VISIBLE);
                    }
                    if (msg_$3.getTime() == null) {
                        msg_$3.setTime("");
                        Mxitong.setVisibility(View.GONE);
                    }else{
                        Mxitong.setVisibility(View.VISIBLE);
                    }
                    if (msg_$3.getSum() == null) {
                        msg_$3.setSum("");
                        Mxitong.setVisibility(View.GONE);
                    }else{
                        Mxitong.setVisibility(View.VISIBLE);
                    }
                    gongzhong.setText(msg_$3.getTitle());
                    numberGuanzhuTime.setText(msg_$3.getTime());
                    numberGuanzhuCount.setText(msg_$3.getSum());
                }
                //挖矿时间
                DetailedClass.MsgBean.ThreevocBean threevocBean = msg.getThreevoc();
                double count1 = threevocBean.get_$_2018070100000088();
                double count2 = threevocBean.get_$_20180630000000175();
                double count3 = threevocBean.get_$_20180629000000285();
                double count4 = threevocBean.get_$_201806280000003();
                double count5 = threevocBean.get_$_2018062700000089();
                double count6 = threevocBean.get_$_2018062600000094();
                double count7 = threevocBean.get_$_2018062500000024();
                double count8 = threevocBean.get_$_20180624000000238();
                double count9 = threevocBean.get_$_2018062300000034();
                double count10 = threevocBean.get_$_201806220000003();
                threevocSantianCount1.setText("+" + count1);
                threevocSantianCount2.setText("+" + count2);
                threevocSantianCount3.setText("+" + count3);
                threevocSantianCount4.setText("+" + count4);
                threevocSantianCount5.setText("+" + count5);
                threevocSantianCount6.setText("+" + count6);
                threevocSantianCount7.setText("+" + count7);
                threevocSantianCount8.setText("+" + count8);
                threevocSantianCount9.setText("+" + count9);
                threevocSantianCount10.setText("+" + count10);
                StringBuffer sb = new StringBuffer();
                try {
                    JSONObject jo = new JSONObject(s);
                    JSONObject jo2 = jo.getJSONObject("msg");
                    JSONObject jo3 = jo2.getJSONObject("threevoc");
                    Iterator<String> keys = jo3.keys();
                    while (keys.hasNext()) {
                        String key = keys.next();
                        sb.append(key + ";");
                    }
                 /*   StringBuffer sb2 = new StringBuffer();
                    JSONObject jo4 = jo2.getJSONObject("today");
                    Iterator<String> key2s = jo4.keys();
                    while (key2s.hasNext()) {
                        String key = key2s.next();
                        String string = jo4.getString(key);
                        today_count.setText("+" + string);
                        sb2.append(key + ";");
                    }*/
                    //前十天的
                    String sbKeys = sb.toString();
                    String keyArr[] = sbKeys.split(";");
                    threevocSantianTime1.setText(isNull(keyArr[0]));
                    threevocSantianTime2.setText(isNull(keyArr[1]));
                    threevocSantianTime3.setText(isNull(keyArr[2]));
                    threevocSantianTime4.setText(isNull(keyArr[3]));
                    threevocSantianTime5.setText(isNull(keyArr[4]));
                    threevocSantianTime6.setText(isNull(keyArr[5]));
                    threevocSantianTime7.setText(isNull(keyArr[6]));
                    threevocSantianTime8.setText(isNull(keyArr[7]));
                    threevocSantianTime9.setText(isNull(keyArr[8]));
                    threevocSantianTime10.setText(isNull(keyArr[9]));


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void initListener() {
        detailedBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private String isNull(String str) {
        if (str == null) {
            return "";
        }
        return str;
    }

/*    // date类型转换为long类型
    // date要转换的date类型的时间
    public  long dateToLong(Date date) {
        return date.getTime();

    }*/
}
