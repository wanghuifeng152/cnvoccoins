package voc.cn.cnvoccoin.entity;

/**
 * Created by Lenovo on 2018/6/29.
 */

public class WalletClass {

    /**
     * code : 1
     * msg : {"uses":8230.630000000003,"use":6230.630000000003,"locking":72.44}
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
         * uses : 8230.630000000003
         * use : 6230.630000000003
         * locking : 72.44
         */

        private double uses;
        private double use;
        private double locking;
        private double voc_coin;

        public double getvoc_coin() {
            return voc_coin;
        }

        public void setvoc_coin(double voc_coin) {
            this.uses = voc_coin;
        }

        public double getUses() {
            return uses;
        }

        public void setUses(double uses) {
            this.uses = uses;
        }

        public double getUse() {
            return use;
        }

        public void setUse(double use) {
            this.use = use;
        }

        public double getLocking() {
            return locking;
        }

        public void setLocking(double locking) {
            this.locking = locking;
        }
    }
}
