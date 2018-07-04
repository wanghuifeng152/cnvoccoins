package voc.cn.cnvoccoin.entity;

import java.util.List;

/**
 * Created by Lenovo on 2018/6/28.
 */

public class AddresClass {


    /**
     * code : 1
     * msg : [{"address":"0x31E20A9e0b50cA9F44dF50381Ca14A6dbEC38bDD","remarks":"于1"},{"address":"0x41E20A9e0b50cA9F44dF50381Ca14A6dbEC38bDD","remarks":"于3"},{"address":"0xueueuieiiduduudjrjrje8e388383e8riririrri","remarks":"于3"},{"address":"0xhduruueueururuururueuueueruuurururruuuru","remarks":"于4"},{"address":"0x1212121212121212121212121212121212121212","remarks":"于6"},{"address":"0xdheuueueuruduufufifiirudjfjjffjfjuruirjj","remarks":"djdh"}]
     * data :
     */

    private int code;
    private String data;
    private List<MsgBean> msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public List<MsgBean> getMsg() {
        return msg;
    }

    public void setMsg(List<MsgBean> msg) {
        this.msg = msg;
    }

    public static class MsgBean {
        /**
         * address : 0x31E20A9e0b50cA9F44dF50381Ca14A6dbEC38bDD
         * remarks : 于1
         */

        private String address;
        private String remarks;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }
    }
}
