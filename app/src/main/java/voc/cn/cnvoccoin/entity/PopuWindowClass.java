package voc.cn.cnvoccoin.entity;

/**
 * Created by Lenovo on 2018/7/13.
 */

public class PopuWindowClass {

    /**
     * code : 1
     * msg : {"code":"获得的VOC初始为锁定状态，自获","codes":"得日后满一个月，变为可提现状态"}
     * data :
     */

    private int code;
    private MsgBean msg;
    private String data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public MsgBean getMsg() {
        return msg;
    }

    public void setMsg(MsgBean msg) {
        this.msg = msg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public static class MsgBean {
        /**
         * code : 获得的VOC初始为锁定状态，自获
         * codes : 得日后满一个月，变为可提现状态
         */

        private String code;
        private String codes;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getCodes() {
            return codes;
        }

        public void setCodes(String codes) {
            this.codes = codes;
        }
    }
}
