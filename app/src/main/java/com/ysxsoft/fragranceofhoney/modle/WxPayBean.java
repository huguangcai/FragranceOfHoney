package com.ysxsoft.fragranceofhoney.modle;

import com.google.gson.annotations.SerializedName;

public class WxPayBean {
    /**
     * code : 0
     * msg : 获取成功！！
     * data : {"prepayid":"wx291011250761785a03fb397e1984423758","appid":"wxc523eebce9e041f3","partnerid":"1524784001","package":"Sign=WXPay","noncestr":"I12985841904548","timestamp":1548727859,"sign":"2C874F75ACC7588C5E4B28D9F98B7A73"}
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
         * prepayid : wx291011250761785a03fb397e1984423758
         * appid : wxc523eebce9e041f3
         * partnerid : 1524784001
         * package : Sign=WXPay
         * noncestr : I12985841904548
         * timestamp : 1548727859
         * sign : 2C874F75ACC7588C5E4B28D9F98B7A73
         */

        private String prepayid;
        private String appid;
        private String partnerid;
        @SerializedName("package")
        private String packageX;
        private String noncestr;
        private int timestamp;
        private String sign;

        public String getPrepayid() {
            return prepayid;
        }

        public void setPrepayid(String prepayid) {
            this.prepayid = prepayid;
        }

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getPartnerid() {
            return partnerid;
        }

        public void setPartnerid(String partnerid) {
            this.partnerid = partnerid;
        }

        public String getPackageX() {
            return packageX;
        }

        public void setPackageX(String packageX) {
            this.packageX = packageX;
        }

        public String getNoncestr() {
            return noncestr;
        }

        public void setNoncestr(String noncestr) {
            this.noncestr = noncestr;
        }

        public int getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(int timestamp) {
            this.timestamp = timestamp;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }
    }
}
