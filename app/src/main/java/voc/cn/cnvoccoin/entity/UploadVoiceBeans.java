package voc.cn.cnvoccoin.entity;

public class UploadVoiceBeans {
    /**
     * code : 1
     * msg : 请求成功!
     * data : {"user":{"id":45283,"user_type":2,"sex":0,"birthday":0,"last_login_time":0,"score":0,"coin":0,"balance":"0.00","create_time":1530552797,"user_status":1,"user_login":"","user_pass":"###ec3ce619fe1e74adea7400d3e0de298e","user_nickname":"","user_email":"","user_url":"","avatar":"","signature":"","last_login_ip":"","user_activation_key":"","mobile":"15801338141","more":null,"voc_coin":"7151.37","isCheck":1,"invite_id":0,"invite_second_id":0},"current":{"voc_coin":0},"next":{"id":4074,"content":"首先，你得先搞清楚一些基本的问题。","voc_coin":1.83},"todayNum":0,"sign":"82340d39dc4a2178bd92b09bd1ccab41#1531454542"}
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
         * user : {"id":45283,"user_type":2,"sex":0,"birthday":0,"last_login_time":0,"score":0,"coin":0,"balance":"0.00","create_time":1530552797,"user_status":1,"user_login":"","user_pass":"###ec3ce619fe1e74adea7400d3e0de298e","user_nickname":"","user_email":"","user_url":"","avatar":"","signature":"","last_login_ip":"","user_activation_key":"","mobile":"15801338141","more":null,"voc_coin":"7151.37","isCheck":1,"invite_id":0,"invite_second_id":0}
         * current : {"voc_coin":0}
         * next : {"id":4074,"content":"首先，你得先搞清楚一些基本的问题。","voc_coin":1.83}
         * todayNum : 0
         * sign : 82340d39dc4a2178bd92b09bd1ccab41#1531454542
         */

        private UserBean user;
        private CurrentBean current;
        private NextBean next;
        private int todayNum;
        private String sign;

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

        public int getTodayNum() {
            return todayNum;
        }

        public void setTodayNum(int todayNum) {
            this.todayNum = todayNum;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public static class UserBean {
            /**
             * id : 45283
             * user_type : 2
             * sex : 0
             * birthday : 0
             * last_login_time : 0
             * score : 0
             * coin : 0
             * balance : 0.00
             * create_time : 1530552797
             * user_status : 1
             * user_login :
             * user_pass : ###ec3ce619fe1e74adea7400d3e0de298e
             * user_nickname :
             * user_email :
             * user_url :
             * avatar :
             * signature :
             * last_login_ip :
             * user_activation_key :
             * mobile : 15801338141
             * more : null
             * voc_coin : 7151.37
             * isCheck : 1
             * invite_id : 0
             * invite_second_id : 0
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
            private Object more;
            private String voc_coin;
            private int isCheck;
            private int invite_id;
            private int invite_second_id;

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

            public Object getMore() {
                return more;
            }

            public void setMore(Object more) {
                this.more = more;
            }

            public String getVoc_coin() {
                return voc_coin;
            }

            public void setVoc_coin(String voc_coin) {
                this.voc_coin = voc_coin;
            }

            public int getIsCheck() {
                return isCheck;
            }

            public void setIsCheck(int isCheck) {
                this.isCheck = isCheck;
            }

            public int getInvite_id() {
                return invite_id;
            }

            public void setInvite_id(int invite_id) {
                this.invite_id = invite_id;
            }

            public int getInvite_second_id() {
                return invite_second_id;
            }

            public void setInvite_second_id(int invite_second_id) {
                this.invite_second_id = invite_second_id;
            }
        }

        public static class CurrentBean {
            /**
             * voc_coin : 0
             */

            private double voc_coin;

            public double getVoc_coin() {
                return voc_coin;
            }

            public void setVoc_coin(double voc_coin) {
                this.voc_coin = voc_coin;
            }
        }

        public static class NextBean {
            /**
             * id : 4074
             * content : 首先，你得先搞清楚一些基本的问题。
             * voc_coin : 1.83
             */

            private int id;
            private String content;
            private double voc_coin;

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

            public double getVoc_coin() {
                return voc_coin;
            }

            public void setVoc_coin(double voc_coin) {
                this.voc_coin = voc_coin;
            }
        }
    }
/**
*《---------------------------------------------==- 线上Java Bean -==---------------------------------------------》
*/

}
