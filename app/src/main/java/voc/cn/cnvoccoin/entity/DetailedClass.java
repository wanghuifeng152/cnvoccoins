package voc.cn.cnvoccoin.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Lenovo on 2018/6/27.
 */

public class DetailedClass {


    /**
     * code : 1
     * msg : {"4":{"sum":null,"time":null,"title":"提现到","address":null},"0":{"sum":666,"time":"2018-07-02 18:07:47","title":"系统奖励"},"1":{"sum":5328,"time":"2018-07-01 18:06:07","title":"邀请好友"},"2":{"sum":666,"time":"2018-06-20 13:20:06","title":"加入社区"},"3":{"sum":666,"time":"2018-06-20 13:20:06","title":"关注公众号"},"threevoc":{"2018-07-01 00:00:00":2113.8599999999997,"2018-06-30 00:00:00":1769.6800000000014,"2018-06-29 00:00:00":157.79000000000008,"2018-06-28 00:00:00":1071.2199999999996,"2018-06-27 00:00:00":3523.390000000002,"2018-06-26 00:00:00":406.98999999999995,"2018-06-25 00:00:00":66,"2018-06-24 00:00:00":77.44,"2018-06-23 00:00:00":0,"2018-06-22 00:00:00":297.90999999999985}}
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
         * 4 : {"sum":null,"time":null,"title":"提现到","address":null}
         * 0 : {"sum":666,"time":"2018-07-02 18:07:47","title":"系统奖励"}
         * 1 : {"sum":5328,"time":"2018-07-01 18:06:07","title":"邀请好友"}
         * 2 : {"sum":666,"time":"2018-06-20 13:20:06","title":"加入社区"}
         * 3 : {"sum":666,"time":"2018-06-20 13:20:06","title":"关注公众号"}
         * threevoc : {"2018-07-01 00:00:00":2113.8599999999997,"2018-06-30 00:00:00":1769.6800000000014,"2018-06-29 00:00:00":157.79000000000008,"2018-06-28 00:00:00":1071.2199999999996,"2018-06-27 00:00:00":3523.390000000002,"2018-06-26 00:00:00":406.98999999999995,"2018-06-25 00:00:00":66,"2018-06-24 00:00:00":77.44,"2018-06-23 00:00:00":0,"2018-06-22 00:00:00":297.90999999999985}
         */

        @SerializedName("4")
        private _$4Bean _$4;
        @SerializedName("0")
        private _$0Bean _$0;
        @SerializedName("1")
        private _$1Bean _$1;
        @SerializedName("2")
        private _$2Bean _$2;
        @SerializedName("3")
        private _$3Bean _$3;
        private ThreevocBean threevoc;

        public _$4Bean get_$4() {
            return _$4;
        }

        public void set_$4(_$4Bean _$4) {
            this._$4 = _$4;
        }

        public _$0Bean get_$0() {
            return _$0;
        }

        public void set_$0(_$0Bean _$0) {
            this._$0 = _$0;
        }

        public _$1Bean get_$1() {
            return _$1;
        }

        public void set_$1(_$1Bean _$1) {
            this._$1 = _$1;
        }

        public _$2Bean get_$2() {
            return _$2;
        }

        public void set_$2(_$2Bean _$2) {
            this._$2 = _$2;
        }

        public _$3Bean get_$3() {
            return _$3;
        }

        public void set_$3(_$3Bean _$3) {
            this._$3 = _$3;
        }

        public ThreevocBean getThreevoc() {
            return threevoc;
        }

        public void setThreevoc(ThreevocBean threevoc) {
            this.threevoc = threevoc;
        }

        public static class _$4Bean {
            /**
             * sum : null
             * time : null
             * title : 提现到
             * address : null
             */

            private Object sum;
            private Object time;
            private String title;
            private Object address;

            public Object getSum() {
                return sum;
            }

            public void setSum(Object sum) {
                this.sum = sum;
            }

            public Object getTime() {
                return time;
            }

            public void setTime(Object time) {
                this.time = time;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public Object getAddress() {
                return address;
            }

            public void setAddress(Object address) {
                this.address = address;
            }
        }

        public static class _$0Bean {
            /**
             * sum : 666
             * time : 2018-07-02 18:07:47
             * title : 系统奖励
             */

            private String sum;
            private String time;
            private String title;

            public String getSum() {
                return sum;
            }

            public void setSum(String sum) {
                this.sum = sum;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }
        }

        public static class _$1Bean {
            /**
             * sum : 5328
             * time : 2018-07-01 18:06:07
             * title : 邀请好友
             */

            private String sum;
            private String time;
            private String title;

            public String getSum() {
                return sum;
            }

            public void setSum(String sum) {
                this.sum = sum;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }
        }

        public static class _$2Bean {
            /**
             * sum : 666
             * time : 2018-06-20 13:20:06
             * title : 加入社区
             */

            private String sum;
            private String time;
            private String title;

            public String getSum() {
                return sum;
            }

            public void setSum(String sum) {
                this.sum = sum;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }
        }

        public static class _$3Bean {
            /**
             * sum : 666
             * time : 2018-06-20 13:20:06
             * title : 关注公众号
             */

            private String sum;
            private String time;
            private String title;

            public String getSum() {
                return sum;
            }

            public void setSum(String sum) {
                this.sum = sum;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }
        }

        public static class ThreevocBean {
            @SerializedName("2018-07-01 00:00:00")
            private double _$_2018070100000088; // FIXME check this code
            @SerializedName("2018-06-30 00:00:00")
            private double _$_20180630000000175; // FIXME check this code
            @SerializedName("2018-06-29 00:00:00")
            private double _$_20180629000000285; // FIXME check this code
            @SerializedName("2018-06-28 00:00:00")
            private double _$_201806280000003; // FIXME check this code
            @SerializedName("2018-06-27 00:00:00")
            private double _$_2018062700000089; // FIXME check this code
            @SerializedName("2018-06-26 00:00:00")
            private double _$_2018062600000094; // FIXME check this code
            @SerializedName("2018-06-25 00:00:00")
            private int _$_2018062500000024; // FIXME check this code
            @SerializedName("2018-06-24 00:00:00")
            private double _$_20180624000000238; // FIXME check this code
            @SerializedName("2018-06-23 00:00:00")
            private int _$_2018062300000034; // FIXME check this code
            @SerializedName("2018-06-22 00:00:00")
            private double _$_201806220000003; // FIXME check this code

            public double get_$_2018070100000088() {
                return _$_2018070100000088;
            }

            public void set_$_2018070100000088(double _$_2018070100000088) {
                this._$_2018070100000088 = _$_2018070100000088;
            }

            public double get_$_20180630000000175() {
                return _$_20180630000000175;
            }

            public void set_$_20180630000000175(double _$_20180630000000175) {
                this._$_20180630000000175 = _$_20180630000000175;
            }

            public double get_$_20180629000000285() {
                return _$_20180629000000285;
            }

            public void set_$_20180629000000285(double _$_20180629000000285) {
                this._$_20180629000000285 = _$_20180629000000285;
            }

            public double get_$_201806280000003() {
                return _$_201806280000003;
            }

            public void set_$_201806280000003(double _$_201806280000003) {
                this._$_201806280000003 = _$_201806280000003;
            }

            public double get_$_2018062700000089() {
                return _$_2018062700000089;
            }

            public void set_$_2018062700000089(double _$_2018062700000089) {
                this._$_2018062700000089 = _$_2018062700000089;
            }

            public double get_$_2018062600000094() {
                return _$_2018062600000094;
            }

            public void set_$_2018062600000094(double _$_2018062600000094) {
                this._$_2018062600000094 = _$_2018062600000094;
            }

            public int get_$_2018062500000024() {
                return _$_2018062500000024;
            }

            public void set_$_2018062500000024(int _$_2018062500000024) {
                this._$_2018062500000024 = _$_2018062500000024;
            }

            public double get_$_20180624000000238() {
                return _$_20180624000000238;
            }

            public void set_$_20180624000000238(double _$_20180624000000238) {
                this._$_20180624000000238 = _$_20180624000000238;
            }

            public int get_$_2018062300000034() {
                return _$_2018062300000034;
            }

            public void set_$_2018062300000034(int _$_2018062300000034) {
                this._$_2018062300000034 = _$_2018062300000034;
            }

            public double get_$_201806220000003() {
                return _$_201806220000003;
            }

            public void set_$_201806220000003(double _$_201806220000003) {
                this._$_201806220000003 = _$_201806220000003;
            }
        }
    }
}
