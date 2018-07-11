package voc.cn.cnvoccoin.entity;

/**
 * Created by WHF
 * on 2018/6/28.
 * at 北京
 */

public class ChargeBean {


    /**
     * code : 1
     * msg : {"charge":1000,"limit":"提现金额不能小于3500"}
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
         * charge : 1000
         * limit : 提现金额不能小于3500
         */

        private int charge;
        private String limit;

        public int getCharge() {
            return charge;
        }

        public void setCharge(int charge) {
            this.charge = charge;
        }

        public String getLimit() {
            return limit;
        }

        public void setLimit(String limit) {
            this.limit = limit;
        }
    }
}

