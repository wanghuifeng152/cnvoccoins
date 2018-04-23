package voc.cn.cnvoccoin.entity;

import android.support.annotation.Keep;

import java.io.Serializable;
import java.util.List;

/**
 * Created by shy on 2018/3/29.
 */
@Keep
public class VoiceTextBean implements Serializable{


    /**
     * code : 1
     * msg : 请求成功!
     * data : [{"id":1,"content":"这是我加入语音链的第一天。"},{"id":2,"content":"我将帮助VOC建立全球语音数据库"},{"id":3,"content":"我将参与保护小语种计划，贡献自己的乡音"},{"id":4,"content":"我会成为语音链的使者，邀请更多训练师，共创梦想"},{"id":5,"content":"《再别康桥》（徐志摩）"},{"id":6,"content":"轻轻的我走了，"},{"id":7,"content":"正如我轻轻的来；"},{"id":8,"content":"我轻轻的招手，"},{"id":9,"content":"作别西天的云彩。"},{"id":10,"content":"那河畔的金柳，"}]
     */

    private int code;
    private String msg;
    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    @Keep
    public static class DataBean implements Serializable{
        /**
         * id : 1
         * content : 这是我加入语音链的第一天。
         */

        private int id;
        private String content;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
