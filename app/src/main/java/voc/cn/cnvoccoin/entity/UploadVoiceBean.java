package voc.cn.cnvoccoin.entity;

import java.io.Serializable;

/**
 * Created by shy on 2018/5/7.
 */

public class UploadVoiceBean implements Serializable,KeepAttr {

    /**
     * user : {"id":2,"user_type":2,"sex":0,"birthday":0,"last_login_time":0,"score":0,"coin":0,"balance":"0.00","create_time":0,"user_status":1,"user_login":"star","user_pass":"","user_nickname":"","user_email":"","user_url":"","avatar":"","signature":"","last_login_ip":"","user_activation_key":"","mobile":"","more":{},"voc_coin":"25398.21"}
     * current : {"voc_coin":"0.1"}
     * next : {"content":"语料文本","voc_coin":"20.00","id":1}
     */

    private UserBean user;
    private CurrentBean current;
    private NextBean next;
    private String todayNum;
    private String sign;

    public String getTodayNum() {
        return todayNum;
    }

    public void setTodayNum(String todayNum) {
        this.todayNum = todayNum;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public CurrentBean getCurrent() {
        return current;
    }

    public void setCurrent(CurrentBean current) {
        this.current = current;
    }

    public NextBean getNext() {
        return next;
    }

    public void setNext(NextBean next) {
        this.next = next;
    }

    public static class UserBean {
        /**
         * id : 2
         * user_type : 2
         * sex : 0
         * birthday : 0
         * last_login_time : 0
         * score : 0
         * coin : 0
         * balance : 0.00
         * create_time : 0
         * user_status : 1
         * user_login : star
         * user_pass :
         * user_nickname :
         * user_email :
         * user_url :
         * avatar :
         * signature :
         * last_login_ip :
         * user_activation_key :
         * mobile :
         * more : {}
         * voc_coin : 25398.21
         */

        private int id;
        private int user_type;
        private int sex;
        private int birthday;
        private int last_login_time;
        private int score;
        private int coin;
        private String balance;
        private int create_time;
        private int user_status;
        private String user_login;
        private String user_pass;
        private String user_nickname;
        private String user_email;
        private String user_url;
        private String avatar;
        private String signature;
        private String last_login_ip;
        private String user_activation_key;
        private String mobile;
        private MoreBean more;
        private String voc_coin;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUser_type() {
            return user_type;
        }

        public void setUser_type(int user_type) {
            this.user_type = user_type;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public int getBirthday() {
            return birthday;
        }

        public void setBirthday(int birthday) {
            this.birthday = birthday;
        }

        public int getLast_login_time() {
            return last_login_time;
        }

        public void setLast_login_time(int last_login_time) {
            this.last_login_time = last_login_time;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public int getCoin() {
            return coin;
        }

        public void setCoin(int coin) {
            this.coin = coin;
        }

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }

        public int getCreate_time() {
            return create_time;
        }

        public void setCreate_time(int create_time) {
            this.create_time = create_time;
        }

        public int getUser_status() {
            return user_status;
        }

        public void setUser_status(int user_status) {
            this.user_status = user_status;
        }

        public String getUser_login() {
            return user_login;
        }

        public void setUser_login(String user_login) {
            this.user_login = user_login;
        }

        public String getUser_pass() {
            return user_pass;
        }

        public void setUser_pass(String user_pass) {
            this.user_pass = user_pass;
        }

        public String getUser_nickname() {
            return user_nickname;
        }

        public void setUser_nickname(String user_nickname) {
            this.user_nickname = user_nickname;
        }

        public String getUser_email() {
            return user_email;
        }

        public void setUser_email(String user_email) {
            this.user_email = user_email;
        }

        public String getUser_url() {
            return user_url;
        }

        public void setUser_url(String user_url) {
            this.user_url = user_url;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public String getLast_login_ip() {
            return last_login_ip;
        }

        public void setLast_login_ip(String last_login_ip) {
            this.last_login_ip = last_login_ip;
        }

        public String getUser_activation_key() {
            return user_activation_key;
        }

        public void setUser_activation_key(String user_activation_key) {
            this.user_activation_key = user_activation_key;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public MoreBean getMore() {
            return more;
        }

        public void setMore(MoreBean more) {
            this.more = more;
        }

        public String getVoc_coin() {
            return voc_coin;
        }

        public void setVoc_coin(String voc_coin) {
            this.voc_coin = voc_coin;
        }

        public static class MoreBean {
        }
    }

    public static class CurrentBean {
        /**
         * voc_coin : 0.1
         */

        private String voc_coin;

        public String getVoc_coin() {
            return voc_coin;
        }

        public void setVoc_coin(String voc_coin) {
            this.voc_coin = voc_coin;
        }
    }

    public static class NextBean {
        /**
         * content : 语料文本
         * voc_coin : 20.00
         * id : 1
         */

        private String content;
        private String voc_coin;
        private int id;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getVoc_coin() {
            return voc_coin;
        }

        public void setVoc_coin(String voc_coin) {
            this.voc_coin = voc_coin;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
