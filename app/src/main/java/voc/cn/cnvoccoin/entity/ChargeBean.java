package voc.cn.cnvoccoin.entity;

/**
 * Created by WHF
 * on 2018/6/28.
 * at 北京
 */

public class ChargeBean {


    /**
     * code : 1
     * msg : {"charge":1000,"uses":9709.210000000003,"use":9709.210000000003,"actual":-999}
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
         * uses : 9709.210000000003
         * use : 9709.210000000003
         * actual : -999
         */

        private int charge;
        private double uses;
        private double use;
        private double actual;

        public int getCharge() {
            return charge;
        }

        public void setCharge(int charge) {
            this.charge = charge;
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

        public double getActual() {
            return actual;
        }

        public void setActual(double actual) {
            this.actual = actual;
        }
    }
}

