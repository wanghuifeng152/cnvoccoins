package voc.cn.cnvoccoin.entity;

/**
 * Created by WHF
 * on 2018/8/3.
 * at 北京
 */

public class Realname {
    /**
     * code : 10000
     * msg : ok
     * data : {"uid":29219,"token":"437cb3ef60384cadacdf55bc74faa21d"}
     */

    private int code;
    private String msg;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * uid : 29219
         * token : 437cb3ef60384cadacdf55bc74faa21d
         */

        private int uid;
        private String token;

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
