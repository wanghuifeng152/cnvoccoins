package voc.cn.cnvoccoin.entity;

import java.util.List;

public class CommunityShequModel {
    /**
     * code : 1
     * msg : 获取成功
     * data : ["http://www.vochain.world/upload/admin/20180529/aa9a367e2e5b70043677825b70d41cd8.png","http://www.vochain.world/upload/admin/20180529/aa9a367e2e5b70043677825b70d41cd8.png"]
     */

    private int code;
    private String msg;
    private List<String> data;

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

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
