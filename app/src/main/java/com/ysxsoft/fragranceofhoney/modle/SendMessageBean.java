package com.ysxsoft.fragranceofhoney.modle;

public class SendMessageBean {
    /**
     * code : 0
     * msg : 获取成功！
     * data : {"password":1}
     */

    private String code;
    private String msg;
    private DataBean data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
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
         * password : 1
         */

        private int password;

        public int getPassword() {
            return password;
        }

        public void setPassword(int password) {
            this.password = password;
        }
    }

//    /**
//     * code : 0
//     * msg : 获取成功！
//     */
//
//    private String code;
//    private String msg;
//
//    public String getCode() {
//        return code;
//    }
//
//    public void setCode(String code) {
//        this.code = code;
//    }
//
//    public String getMsg() {
//        return msg;
//    }
//
//    public void setMsg(String msg) {
//        this.msg = msg;
//    }



}
