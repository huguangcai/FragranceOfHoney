package com.ysxsoft.fragranceofhoney.modle;

/**
 * Create By 胡
 * on 2019/10/14 0014
 */
public class VersionBean {

    /**
     * code : 0
     * msg : 获取成功
     * data : {"version":"v1.0.2","link":""}
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
         * version : v1.0.2
         * link :
         */

        private String version;
        private String link;

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }
    }
}
