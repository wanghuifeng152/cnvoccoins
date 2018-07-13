package voc.cn.cnvoccoin.entity;

import java.util.List;

public class TaskEntity {


    /**
     * code : 1
     * msg : 请求成功
     * data : [{"taskStatus":0,"task":"加入群组","task_id":2,"string":"2018最落地，区块链+智能语音大数据场景应用。比特币挖矿模式，拒绝传销，拒绝虚拟矿机，社区成员多劳多得。个人语音就能挖矿，打造去中心全球公有语音大数据库。已和国内知名语音识别公司展开合作，市场千亿市值，注册即送1350VOC，一枚价值0.1元，每天朗读15分钟，轻松获得2000币，价值200元。立刻下载：http://www.vochain.world/user/index/htmlregist.html?invite_code=45283"},{"taskStatus":1,"task":"关注公众号","task_id":3,"string":"2018最落地，区块链+智能语音大数据场景应用。比特币挖矿模式，拒绝传销，拒绝虚拟矿机，社区成员多劳多得。个人语音就能挖矿，打造去中心全球公有语音大数据库。已和国内知名语音识别公司展开合作，市场千亿市值，注册即送1350VOC，一枚价值0.1元，每天朗读15分钟，轻松获得2000币，价值200元。立刻下载：http://www.vochain.world/user/index/htmlregist.html?invite_code=45283"}]
     */

    private int code;
    private String msg;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * taskStatus : 0
         * task : 加入群组
         * task_id : 2
         * string : 2018最落地，区块链+智能语音大数据场景应用。比特币挖矿模式，拒绝传销，拒绝虚拟矿机，社区成员多劳多得。个人语音就能挖矿，打造去中心全球公有语音大数据库。已和国内知名语音识别公司展开合作，市场千亿市值，注册即送1350VOC，一枚价值0.1元，每天朗读15分钟，轻松获得2000币，价值200元。立刻下载：http://www.vochain.world/user/index/htmlregist.html?invite_code=45283
         */

        private int taskStatus;
        private String task;
        private int task_id;
        private String string;

        public int getTaskStatus() {
            return taskStatus;
        }

        public void setTaskStatus(int taskStatus) {
            this.taskStatus = taskStatus;
        }

        public String getTask() {
            return task;
        }

        public void setTask(String task) {
            this.task = task;
        }

        public int getTask_id() {
            return task_id;
        }

        public void setTask_id(int task_id) {
            this.task_id = task_id;
        }

        public String getString() {
            return string;
        }

        public void setString(String string) {
            this.string = string;
        }
    }
}
