package voc.cn.cnvoccoin.entity;

import android.view.View;

import java.util.List;

public class TaskEntity {

    /**
     * code : 1
     * msg : 靖求成功
     * data : [{"taskstatus":0,"task":"加入群組"},{"taskStatus":1,"task":"美注公僉号"}]
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
         * taskstatus : 0
         * task : 加入群組
         * taskStatus : 1
         */

        private int taskstatus;
        private String task;
        private int taskStatus;

        public int getTaskstatus() {
            return taskstatus;
        }

        public void setTaskstatus(int taskstatus) {
            this.taskstatus = taskstatus;
        }

        public String getTask() {
            return task;
        }

        public void setTask(String task) {
            this.task = task;
        }

        public int getTaskStatus() {
            return taskStatus;
        }

        public void setTaskStatus(int taskStatus) {
            this.taskStatus = taskStatus;
        }
    }
}
