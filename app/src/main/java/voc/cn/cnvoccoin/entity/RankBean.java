package voc.cn.cnvoccoin.entity;

import android.support.annotation.Keep;

import java.io.Serializable;
import java.util.List;

/**
 * Created by shy on 2018/5/8.
 */
@Keep
public class RankBean implements Serializable,KeepAttr {

    /**
     * code : 1
     * msg : 请求成功!
     * data : {"list":[{"userId":5,"coinSum":1009,"rankNum":1,"userAccount":"18712345678"},{"userId":3,"coinSum":1006,"rankNum":2,"userAccount":"18712345670"},{"userId":1,"coinSum":1003,"rankNum":3,"userAccount":"18712345671"},{"userId":7,"coinSum":1003,"rankNum":4,"userAccount":"18712345672"},{"userId":6,"coinSum":1002,"rankNum":5,"userAccount":"18712345673"}]}
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
        private List<ListBean> list;

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean{
            /**
             * userId : 5
             * coinSum : 1009
             * rankNum : 1
             * userAccount : 18712345678
             */

            private int userId;
            private int coinSum;
            private int rankNum;
            private String userAccount;

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public int getCoinSum() {
                return coinSum;
            }

            public void setCoinSum(int coinSum) {
                this.coinSum = coinSum;
            }

            public int getRankNum() {
                return rankNum;
            }

            public void setRankNum(int rankNum) {
                this.rankNum = rankNum;
            }

            public String getUserAccount() {
                return userAccount;
            }

            public void setUserAccount(String userAccount) {
                this.userAccount = userAccount;
            }
        }
    }
}
