package voc.cn.cnvoccoin.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import voc.cn.cnvoccoin.R;
import voc.cn.cnvoccoin.entity.AddresClass;
import voc.cn.cnvoccoin.entity.DetailedClass;

/**
 * Created by Lenovo on 2018/6/27.
 */

public class DetailedAdapter extends BaseAdapter {
    private Context mContext;
    private List<AddresClass.MsgBean> addresClassMsg;
    private int checked = 0;

    public void setChecked(int checked){
        this.checked=checked;
    }


    public DetailedAdapter(Context mContext, List<AddresClass.MsgBean> addresClassMsg) {
        this.mContext = mContext;
        this.addresClassMsg = addresClassMsg;
    }

    @Override
    public int getCount() {
        return addresClassMsg.size();
    }

    @Override
    public Object getItem(int position) {
        return addresClassMsg.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.adapter_lv, null);
            viewHolder = new ViewHolder();

           viewHolder.addImg = convertView.findViewById(R.id.addImg);
           viewHolder.addItem = convertView.findViewById(R.id.addItem);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.addItem.setText(addresClassMsg.get(position).getRemarks());

        if(checked==position){
            viewHolder.addImg.setImageResource(R.mipmap.icon_choose1);
        }else {
            viewHolder.addImg.setImageResource(R.mipmap.icon_choose);
        }
        return convertView;
    }

    class ViewHolder {
        ImageView addImg;
        TextView addItem;
    }
}
