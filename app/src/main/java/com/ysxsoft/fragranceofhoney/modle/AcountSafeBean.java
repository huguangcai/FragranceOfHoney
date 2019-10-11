package com.ysxsoft.fragranceofhoney.modle;

public class AcountSafeBean {

    /**
     * code : 0
     * msg : 获取成功！
     * data : {"mobile":"15638922800","tradepassword":2}
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
         * mobile : 15638922800
         * tradepassword : 2
         */

        private String mobile;
        private String tradepassword;

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getTradepassword() {
            return tradepassword;
        }

        public void setTradepassword(String tradepassword) {
            this.tradepassword = tradepassword;
        }
    }
}
